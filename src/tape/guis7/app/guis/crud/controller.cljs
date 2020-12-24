(ns tape.guis7.app.guis.crud.controller
  (:refer-clojure :rename {update update-})
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc.controller :as c :include-macros true]
            [tape.mvc.view :as v :include-macros true]
            [tape.router :as router]
            [tape.toasts.controller :as toasts.c]
            [tape.guis7.app.guis.crud.model :as crud.m]))

;;; Routes

(def ^{::c/reg ::c/routes} routes
  ["/crud" {:coercion rcs/coercion}
   ["" ::index]
   ["/new" {:name ::new :conflicting true}]
   ["/:id" {:conflicting true, :parameters {:path {:id int?}}}
    ["/edit" ::edit]]])

;;; Handlers

(defn index
  {::c/reg ::c/event-db}
  [db _]
  (cond-> (dissoc db ::v/current)
          (nil? (::people db)) (assoc ::people crud.m/seed)))

(defn new
  {::c/reg ::c/event-db}
  [db _]
  (-> db
      (assoc ::person {})
      (dissoc ::v/current)))

(defn edit
  {::c/reg ::c/event-db}
  [db [_ params]]
  (-> db
      (assoc ::person (get-in db [::people (-> params :path :id)]))
      (dissoc ::v/current)))

(defn create
  {::c/reg ::c/event-fx}
  [{:keys [db]} _]
  {:db         (update- db ::people crud.m/insert (::person db))
   :dispatch-n [[::router/navigate [::index]]
                [::toasts.c/create :success "Person created"]]})

(defn update
  {::c/reg ::c/event-fx}
  [{:keys [db]} [_ params]]
  {:db         (update- db ::people assoc (-> params :path :id) (::person db))
   :dispatch-n [[::router/navigate [::index]]
                [::toasts.c/create :success "Person updated"]]})

(defn delete
  {::c/reg ::c/event-fx}
  [{:keys [db]} [_ args]]
  {:db       (update- db ::people dissoc (-> args :id))
   :dispatch [::toasts.c/create :success "Person deleted"]})

(defn field
  {::c/reg ::c/event-db}
  [db [_ k v]] (assoc-in db [::person k] v))

;;; Sub

(defn people
  {::c/reg ::c/sub}
  [db _] (::people db))

(defn person
  {::c/reg ::c/sub}
  [db _] (::person db))

;;; Module

(c/defmodule)
