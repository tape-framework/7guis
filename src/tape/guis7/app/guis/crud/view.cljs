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
       [field :first-name]]]
     [:div.field
      [:label.label "Last name"]
      [:div.control
       [field :last-name]]]]))

;;; Views

(defn ^::v/view index []
  (let [people @(v/subscribe [crud.c/people])]
    [:div.is-bound
     [header "List people"
      [:a.button.is-primary {:href (router/href [crud.c/new])} "New"]]
     [:table.table
      [:tbody
       (for [[_ person] people
             :let [{:keys [id first-name last-name]} person]]
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

(defn ^::v/view show []
  (let [{:keys [id first-name last-name]} @(v/subscribe [crud.c/person])]
    [:article.article.is-bound
     [header "Show person"
      [:a.button.is-primary {:href (router/href [crud.c/edit {:id id}])} "Edit"]
      [:button.button.is-danger
       {:on-click #(v/dispatch [crud.c/delete])} "Delete"]]
     [:p first-name last-name]]))

(defn ^::v/view new []
  [:form.is-bound
   [header "New person"
    [:button.button.is-primary
     {:on-click (form/when-valid #(v/dispatch [crud.c/create]))} "Create"]]
   [form-fields]])

(defn ^::v/view edit []
  (let [{:keys [id]} @(v/subscribe [crud.c/person])]
    [:form.is-bound
     [header "Edit person"
      [:button.button.is-primary
       {:on-click (form/when-valid
                   #(v/dispatch [crud.c/update {:path {:id id}}]))} "Update"]]
     [form-fields]]))

;;; Module

(v/defmodule tape.guis7.app.guis.crud.controller)
