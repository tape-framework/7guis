(ns tape.guis7.app.guis.counter.view
  (:require [tape.mvc :as mvc :include-macros true]
            [tape.router :as router :include-macros true]
            [tape.guis7.app.guis.counter.controller :as counter.c]))

;;; Views

(defn index
  {::mvc/reg ::mvc/view}
  []
  (let [cnt @(mvc/subscribe [counter.c/count])]
    [:div.field.has-addons.is-bound
     [:div.control
      [:input.input {:type "number" :value cnt :read-only true}]]
     [:div.control
      [:a.button.is-info {:href (router/href [counter.c/increment])} "Count"]]]))

;;; Module

(mvc/defm ::module)
