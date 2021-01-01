(ns tape.guis7.app.guis.cells.view
  (:require [reagent.core :as r]
            [tape.mvc :as mvc :include-macros true]
            [tape.guis7.app.guis.cells.controller :as cells.c]))

;;; Helpers

(def ^:private cols (map char (range 65 70)))

;;; Partials

(defn- cell [m]
  (let [focused (r/atom false)
        toggle #(swap! focused not)]
    (fn [m]
      (let [{:keys [i j]} m
            {:keys [value in-formula]} @(mvc/subscribe [cells.c/cell i j])
            recompute #(mvc/dispatch [cells.c/recompute i j])
            set-cell (fn [event]
                       (let [in-formula (-> event .-target .-value)]
                         (mvc/dispatch [cells.c/set-formula i j in-formula])))]
        [:input.input {:value (if @focused in-formula value)
                       :on-focus toggle
                       :on-blur #(do (toggle) (recompute))
                       :on-change set-cell}]))))

;;; Views

(defn index
  {::mvc/reg ::mvc/view}
  []
  [:table.table
   [:tbody
    [:tr
     [:th " "]
     (for [c cols] [:th {:key c} c])]
    (for [i (range 5)]
      [:tr {:key i}
       [:td i]
       (for [j (range 5)]
         [:td {:key j}
          [cell {:i i :j j}]])])]])

;;; Module

(mvc/defm ::module)
