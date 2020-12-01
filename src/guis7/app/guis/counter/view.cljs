(ns guis7.app.guis.counter.view
  (:require [re-frame.core :as rf]
            [tape.mvc.view :as v :include-macros true]
            [tape.router :as router]
            [guis7.app.guis.counter.controller :as counter.c]))

;;; Views

(defn ^::v/view index []
  (let [cnt @(rf/subscribe [::counter.c/count])]
    [:div.field.has-addons.form-bound
     [:div.control
      [:input.input {:type "number" :value cnt :read-only true}]]
     [:div.control
      [:a.button.is-info {:href (router/href [::counter.c/increment])} "Count"]]]))

;;; Module

(v/defmodule guis7.app.guis.counter.controller)
