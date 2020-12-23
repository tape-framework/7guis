;; From https://github.com/eugenkiss/7guis-Clojure-Seesaw/blob/master/src/sevenguis/cells.clj
(ns tape.guis7.app.guis.cells.model
  (:refer-clojure :rename {eval eval-})
  (:require [cljs.reader :as reader]
            [clojure.string :as string]
            [instaparse.core :as instaparse]))

;;; Domain

(defn type-str [t]
  (last (string/split (str (type t)) #"\.")))

(defprotocol Formula
  (eval [this data])
  (refs [this data])
  (to-str [this]))

(defrecord Textual [value]
  Formula
  (eval [this data] "")
  (refs [this data] [])
  (to-str [this] value))

(defrecord Decimal [value]
  Formula
  (eval [this data] value)
  (refs [this data] [])
  (to-str [this] (str value)))

(defrecord Coord [row column]
  Formula
  (eval [this data] (:value (get data [row column])))
  (refs [this data] [(get data [row column])])
  (to-str [this] (str (char (+ 65 column)) row)))

(defrecord Rangef [coord1 coord2]
  Formula
  (eval [this data] js/NaN)
  (refs [this data] (for [row (range (:row coord1) (inc (:row coord2)))
                          col (range (:column coord1) (inc (:column coord2)))]
                      (get data [row col])))
  (to-str [this] (str (to-str coord1) ":" (to-str coord2))))

(def op-table
  {"add"  #(+ %1 %2)
   "sub"  #(- %1 %2)
   "div"  #(/ %1 %2)
   "mul"  #(* %1 %2)
   "mod"  #(mod %1 %2)
   "sum"  +
   "prod" *})

(defn eval-list [formula data]
  (if (= "Rangef" (type-str formula))
    (map #(:value %) (refs formula data))
    [(eval formula data)]))

(defrecord Application [function arguments]
  Formula
  (eval [this data]
    (let [argvals (mapcat #(eval-list % data) arguments)]
      (try
        (apply (get op-table function) argvals)
        (catch js/Error e js/NaN))))
  (refs [this data] (mapcat #(refs % data) arguments))
  (to-str [this] (str function "(" (string/join ", " (map to-str arguments)) ")")))

(def Emptyf (Textual. ""))

(defn parse-formula [formula-str]
  (let [result
        ((instaparse/parser "
          formula = decimal / textual / (<'='> expr)
          expr    = range / cell / decimal / app
          app     = ident <'('> (expr <','>)* expr <')'>
          range   = cell <':'> cell
          cell    = #'[A-Za-z]\\d+'
          textual = #'[^=].*'
          ident   = #'[a-zA-Z_]\\w*'
          decimal = #'-?\\d+(\\.\\d*)?'
          ") formula-str)]
    (if (instaparse/failure? result)
      (Textual. (str (instaparse/get-failure result)))
      (instaparse/transform
       {:decimal #(Decimal. (js/parseFloat %))
        :ident   str
        :textual #(Textual. %)
        :cell    #(Coord. (reader/read-string (subs % 1)) (- (.charCodeAt % 0) 65))
        :range   #(Rangef. %1 %2)
        :app     (fn [f & as] (Application. f (vec as)))
        :expr    identity
        :formula identity
        } result))))

(defn make-data [height width]
  (into {} (for [x (range height) y (range width)]
             [[x y] {:x x :y y :value "" :in-formula "" :formula Emptyf :observers []}])))

(defn cell-str [{value :value formula :formula}]
  (if (= "Textual" (type-str formula))
    (to-str formula)
    (str value)))

;;; GUI

(defn change-prop [{x :x y :y v :value f :formula os :observers} data]
  (let [nv (eval f @data)]
    (when-not (or (= v nv) (and (js/isNaN v) (js/isNaN nv)))
      (swap! data assoc-in [[x y] :value] nv)
      (doseq [[x y] os] (change-prop (get data [x y]) data)))))

(defn edit [data i j]
  (let [in-formula  (:in-formula (get @data [i j]))
        formula     (parse-formula (or in-formula ""))
        old-formula (:formula (get @data [i j]))]
    (doseq [cell (refs old-formula @data)]
      (swap! data update-in [[(:x cell) (:y cell)] :observers]
             (fn [obs] (remove #(= % [i j]) obs))))
    (doseq [cell (refs formula @data)]
      (swap! data update-in [[(:x cell) (:y cell)] :observers]
             #(conj % [i j])))
    (swap! data assoc-in [[i j] :formula] formula)
    (change-prop (get @data [i j]) data)))
