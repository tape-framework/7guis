(ns guis7.app.guis.flight-booker.model
  (:require [clojure.string :as string]
            [validateur.validation :as validation]
            [cljs-time.format :as format]))

(defn- is-date-string [s]
  (if (string/blank? s)
    true
    (try
      (format/parse (:date format/formatters) s)
      true
      (catch js/Error e
        false))))

(def validate
  (validation/validation-set
   (validation/presence-of :kind)
   (validation/inclusion-of :kind :in #{"one" "return"})
   (validation/presence-of :leave)
   (validation/validate-by :leave is-date-string)
   (validation/validate-when #(= (:kind %) "return")
                             (validation/presence-of :return))
   (validation/validate-by :return is-date-string)))

(defn add-field
  "A setter of sorts, adds the k-v to the map and updates errors and derivatives."
  [old-booking old-errors k v]
  (let [new-booking  (assoc old-booking k v)
        new-errors   (validate new-booking)
        new-booking' (assoc new-booking :valid (empty? new-errors))
        field-errors (get new-errors k)
        ;; we want to affect errors only on the field being edited
        out-errors   (assoc old-errors k field-errors)]
    ;; clean `leave` when disabled
    (if (and (= k :kind) (= v "one"))
      [(dissoc new-booking' :return) (dissoc out-errors :return)]
      [new-booking' out-errors])))
