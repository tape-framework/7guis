(ns tape.guis7.app.guis.flight-booker.view
  (:require [tape.mvc.view :as v :include-macros true]
            [tape.tools :as tools :include-macros true]
            [tape.tools.ui.form :as form]
            [tape.guis7.app.guis.flight-booker.controller :as flight-booker.c]
            [clojure.string :as string]))

;;; Helpers

(def ^:private kind-options {"one" "One way flight", "return" "Return flight"})

;;; Views

(defn index
  {::v/reg ::v/view}
  []
  (let [lens   (tools/lens flight-booker.c/booking flight-booker.c/field)
        kind   (lens [:kind])
        leave  (lens [:leave])
        return (lens [:return])
        errors (lens [:errors])
        valid  (lens [:valid])
        booked (lens [:booked])
        book   #(when valid (tools/dispatch [flight-booker.c/book]))]
    [:div.is-bound
     [:div.field
      [:div.control
       [:label.label "Kind"]
       [:div.select
        [form/field {:type :select, :source lens, :field :kind, :options kind-options}]]]]
     [:div.field
      [:div.control
       [:label.label "Leave"]
       [form/field-with-list-errors {:type   :date, :source lens, :field :leave, :class ["input"],
                                     :errors (:leave errors)}]]]
     [:div.field
      [:div.control
       [:label.label "Return"]
       [form/field-with-list-errors {:type     :date, :source lens, :field :return, :class ["input"],
                                     :disabled (= kind "one"), :errors (:return errors)}]]]
     [:div.field.is-grouped
      [:div.control
       [:a.button.is-info {:on-click book, :disabled (not valid)} "Book"]]
      (if booked
        [:div.control.is-flex.is-align-items-center
         [:p (str "You have booked a " (string/lower-case (get kind-options kind))
                  " flight on " leave
                  (when (= kind "return")
                    (str " and " return)))]])]]))

;;; Module

(v/defmodule)
