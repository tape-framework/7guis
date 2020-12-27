(ns tape.guis7.app.guis.temperature-converter.view
  (:require [tape.mvc.view :as v :include-macros true]
            [tape.tools :as tools :include-macros true]
            [tape.guis7.app.guis.temperature-converter.controller :as temperature-converter.c]))

;;; Views

(defn index
  {::v/reg ::v/view}
  []
  (let [celsius @(tools/subscribe [temperature-converter.c/celsius])
        fahrenheit @(tools/subscribe [temperature-converter.c/fahrenheit])
        from-celsius #(tools/dispatch [temperature-converter.c/from-celsius
                                       (-> % .-target .-value)])
        from-fahrenheit #(tools/dispatch
                          [temperature-converter.c/from-fahrenheit
                           (-> % .-target .-value)])]
    [:div.field.columns.is-bound
     [:div.control.column
      [:label.label "Celsius"]
      [:input.input {:type "number" :value (or celsius "")
                     :on-change from-celsius}]]
     [:div.control.is-align-self-center "="]
     [:div.control.column
      [:label.label "Fahrenheit"]
      [:input.input {:type "number" :value (or fahrenheit "")
                     :on-change from-fahrenheit}]]]))

;;; Module

(v/defmodule)
