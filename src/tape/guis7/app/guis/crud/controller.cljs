(ns tape.guis7.app.guis.crud.controller
  "People CRUD persisted to localstorage via datascript."
  {:tape.mvc.controller/interceptors [datascript.c/inject]}
  (:refer-clojure :rename {list list-})
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc.controller :as c :include-macros true]
            [tape.mvc.view :as v :include-macros true]
            [tape.router :as router]
            [tape.toasts.controller :as toasts.c]
            [tape.datascript.controller :as datascript.c]
            [tape.guis7.app.guis.crud.model :as model.m]))

;;; Routes

(def ^{::c/reg ::c/routes} routes
  ["/crud" {:coercion rcs/coercion}
   ["" ::index]
   ["/list" ::list]
   ["/new" {:name ::new :conflicting true}]
   ["/:id" {:conflicting true, :parameters {:path {:id int?}}}
    ["/edit" ::edit]]])

;;; Handlers

(defn index
  "Seed and redirect to list."
  {::c/reg ::c/event-fx}
  [{::datascript.c/keys [ds]} _]
  {:fx [[::datascript.c/ds (model.m/maybe-seed ds)]
        [:dispatch [::router/navigate [::list]]]]})

(defn list
  {::c/reg ::c/event-fx}
  [{::datascript.c/keys [ds] :keys [db]} _]
  {:db (-> db
           (assoc ::v/current ::index)
           (assoc ::people (model.m/all ds)))
   ::datascript.c/dump true})

(defn new
  {::c/reg ::c/event-db}
  [db _]
  (-> db
      (dissoc ::v/current)
      (assoc ::person {})))

(defn edit
  {::c/reg ::c/event-fx}
  [{::datascript.c/keys [ds] :keys [db]} [_ params]]
  (let [id (-> params :path :id)]
    {:db (-> db
             (dissoc ::v/current)
             (assoc ::person (model.m/one ds id)))}))

(defn save
  {::c/reg ::c/event-fx}
  [{::datascript.c/keys [ds] :keys [db]} _]
  {::datascript.c/ds (model.m/save ds (::person db))
   :dispatch-n [[::router/navigate [::list]]
                [::toasts.c/create :success "Person saved"]]})

(defn delete
  {::c/reg ::c/event-fx}
  [{::datascript.c/keys [ds]} [_ args]]
  (let [id (:id args)]
    {::datascript.c/ds (model.m/delete ds id)
     :dispatch-n [[::router/navigate [::list]]
                  [::toasts.c/create :success "Person deleted"]]}))

(defn field
  {::c/reg ::c/event-db}
  [db [_ k v]]
  (assoc-in db [::person k] v))

;;; Sub

(defn people
  {::c/reg ::c/sub}
  [db _] (::people db))

(defn person
  {::c/reg ::c/sub}
  [db _] (::person db))

;;; Module

(c/defmodule)
