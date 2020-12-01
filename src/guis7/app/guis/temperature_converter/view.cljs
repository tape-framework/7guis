(ns guis7.app.guis.temperature-converter.view
  (:require [re-frame.core :as rf]
            [tape.mvc.view :as v :include-macros true]
            [guis7.app.guis.temperature-converter.controller :as temperature-converter.c]))

;;; Views

(defn ^::v/view index []
  (let [celsius @(rf/subscribe [::temperature-converter.c/celsius])
        farenheit @(rf/subscribe [::temperature-converter.c/farenheit])
        from-celsius #(rf/dispatch [::temperature-converter.c/from-celsius (-> % .-target .-value)])
        from-farenheit #(rf/dispatch [::temperature-converter.c/from-farenheit (-> % .-target .-value)])]
    [:div.field.columns
     [:div.control.column
      [:label.label "Celsius"]
      [:input.input {:type "number" :value (or celsius "") :on-change from-celsius}]]
     [:div.control.is-align-self-center "="]
     [:div.control.column
      [:label.label "Farenheit"]
      [:input.input {:type "number" :value (or farenheit "") :on-change from-farenheit}]]]))

;;; Module

(v/defmodule guis7.app.guis.temperature-converter.controller)
