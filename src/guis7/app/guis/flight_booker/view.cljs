(ns guis7.app.guis.flight-booker.view
  (:require [re-frame.core :as rf]
            [tape.mvc.view :as v :include-macros true]
            [tape.router :as router]
            [tape.tools :as tools]
            [tape.tools.ui.form :as form]
            [guis7.app.guis.flight-booker.controller :as flight-booker.c]
            [clojure.string :as string]))

;;; Helpers

(def ^:private kind-options {"one" "One way flight", "return" "Return flight"})

;; TODO: port field-with-errors to tools
(defn- field-with-errors [m]
  [form/field (cond-> (dissoc m :errors)
                      (some? (:errors m)) (update :class conj "is-danger"))])

(defn- list-errors [errors]
  (for [error errors]
    [:p.help.is-danger {:key error} error]))

;;; Views

(defn ^::v/view index []
  (let [lens   (tools/lens ::flight-booker.c/booking ::flight-booker.c/field)
        errors @(rf/subscribe [::flight-booker.c/booking-errors])
        kind   (lens [:kind])
        leave  (lens [:leave])
        return (lens [:return])
        valid  (lens [:valid])
        booked (lens [:booked])]
    [:div.form-bound
     [:div.field
      [:div.control
       [:label.label "Kind"]
       [:div.select
        [form/field {:type :select, :source lens, :field :kind, :options kind-options}]]]]
     [:div.field
      [:div.control
       [:label.label "Leave"]
       [field-with-errors {:type :date, :source lens, :field :leave, :class ["input"], :errors (:leave errors)}]
       (list-errors (:leave errors))]]
     [:div.field
      [:div.control
       [:label.label "Return"]
       [field-with-errors {:type     :date, :source lens, :field :return, :class ["input"], :errors (:return errors)
                           :disabled (= kind "one")}]
       (list-errors (:return errors))]]
     [:div.field.is-grouped
      [:div.control
       [:a.button.is-info {:href (router/href [::flight-booker.c/book])
                           :disabled (not valid)} "Book"]]
      (if booked
        [:div.control.is-flex.is-align-items-center
         [:p (str "You have booked a " (string/lower-case (get kind-options kind))
                  " flight on " leave
                  (when (= kind "return")
                    (str " and " return)))]])]]))

;;; Module

(v/defmodule guis7.app.guis.flight-booker.controller)
