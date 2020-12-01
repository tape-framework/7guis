(ns guis7.app.guis.temperature-converter.view
  (:require [re-frame.core :as rf]
            [tape.mvc.view :as v :include-macros true]
            [guis7.app.guis.temperature-converter.controller :as temperature-converter.c]))

;;; Views

(defn ^::v/view index []
  (let [celsius         @(rf/subscribe [::temperature-converter.c/celsius])
        fahrenheit      @(rf/subscribe [::temperature-converter.c/fahrenheit])
        from-celsius    #(rf/dispatch [::temperature-converter.c/from-celsius (-> % .-target .-value)])
        from-fahrenheit #(rf/dispatch [::temperature-converter.c/from-fahrenheit (-> % .-target .-value)])]
    [:div.field.columns.form-bound
     [:div.control.column
      [:label.label "Celsius"]
      [:input.input {:type "number" :value (or celsius "") :on-change from-celsius}]]
     [:div.control.is-align-self-center "="]
     [:div.control.column
      [:label.label "Fahrenheit"]
      [:input.input {:type "number" :value (or fahrenheit "") :on-change from-fahrenheit}]]]))

;;; Module

(v/defmodule guis7.app.guis.temperature-converter.controller)
