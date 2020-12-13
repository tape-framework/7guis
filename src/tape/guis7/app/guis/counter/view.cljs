(ns tape.guis7.app.guis.counter.view
  (:require [tape.mvc.view :as v :include-macros true]
            [tape.router :as router :include-macros true]
            [tape.guis7.app.guis.counter.controller :as counter.c]))

;;; Views

(defn ^::v/view index []
  (let [cnt @(v/subscribe [counter.c/count])]
    [:div.field.has-addons.is-bound
     [:div.control
      [:input.input {:type "number" :value cnt :read-only true}]]
     [:div.control
      [:a.button.is-info {:href (router/href [counter.c/increment])} "Count"]]]))

;;; Module

(v/defmodule tape.guis7.app.guis.counter.controller)
