(ns tape.guis7.app.guis.crud.view
  (:require [reagent.core :as r]
            [tape.mvc.view :as v :include-macros true]
            [tape.router :as router :include-macros true]
            [tape.tools :as tools :include-macros true]
            [tape.tools.ui.form :as form]
            [tape.guis7.app.guis.crud.controller :as crud.c]))

;;; Partials

(defn- header [title & buttons]
  [:h2.subtitle title
   (into [:span.buttons.is-pulled-right] buttons)])

(defn- form-fields []
  (let [lens  (tools/lens crud.c/person crud.c/field)
        field (fn [field]
                (form/field {:type :text, :class "input", :source lens,
                             :field field, :required true}))]
    [:<>
     [:div.field
      [:label.label "First name"]
      [:div.control
       [field :person/first-name]]]
     [:div.field
      [:label.label "Last name"]
      [:div.control
       [field :person/last-name]]]]))

;;; Views

(defn index
  {::v/reg ::v/view}
  []
  (let [people @(v/subscribe [crud.c/people])]
    [:div.is-bound
     [header "List people"
      [:a.button.is-primary {:href (router/href [crud.c/new])} "New"]]
     [:table.table
      [:tbody
       (for [person people
             :let [{:db/keys [id] :person/keys [first-name last-name]} person]]
         [:tr {:key id}
          [:td first-name]
          [:td last-name]
          [:td
           [:div.field.has-addons
            [:div.control
             [:a.button {:href (router/href [crud.c/edit {:id id}])} "Edit"]]
            [:div.control
             [:button.button.is-danger
              {:on-click #(v/dispatch [crud.c/delete {:id id}])}
              "Delete"]]]]])]]]))

(defn new
  {::v/reg ::v/view}
  []
  [:form.is-bound
   [header "New person"
    [:button.button.is-primary
     {:on-click (form/when-valid #(v/dispatch [crud.c/save]))} "Create"]]
   [form-fields]])

(defn edit
  {::v/reg ::v/view}
  []
  [:form.is-bound
   [header "Edit person"
    [:button.button.is-primary
     {:on-click (form/when-valid #(v/dispatch [crud.c/save]))} "Update"]]
   [form-fields]])

;;; Module

(v/defmodule)
