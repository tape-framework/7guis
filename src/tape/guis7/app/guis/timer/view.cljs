(ns tape.guis7.app.guis.timer.view
  (:require [reagent.core :as r]
            [tape.mvc :as mvc :include-macros true]
            [tape.router :as router :include-macros true]
            [tape.tools.ui.form :as form :include-macros true]
            [tape.guis7.app.guis.timer.controller :as timer.c]))

;;; Helpers

(defn- progress-percent [duration progress]
  (-> progress (* 100.0) (/ duration)))

;;; Views

(defn index
  {::mvc/reg ::mvc/view}
  []
  (let [lens (form/lens timer.c/timer timer.c/field)
        progress (lens [:progress])
        duration (r/cursor lens [:duration])]
    [:div.is-bound
     [:div.field
      [:div.label "Elapsed time:"]
      [:div.control
       [:progress.progress.is-primary
        {:value (progress-percent @duration progress) :max 100}]
       [:p progress]]]
     [:div.field
      [:div.label "Duration"]
      [:div.control
       [:input.is-fullwidth
        {:type "range" :step 0.1 :min 0 :max 100
         :value @duration
         :on-change #(reset! duration (-> % .-target .-value js/parseFloat))}]]]
     [:div.control
      [:a.button.is-info {:href (router/href [timer.c/index])} "Reset"]]]))

;;; Module

(mvc/defm ::module)
