(ns tape.guis7.app.guis.crud.controller
  (:refer-clojure :rename {update update-})
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc.controller :as c :include-macros true]
            [tape.mvc.view :as v :include-macros true]
            [tape.router :as router]
            [tape.toasts.controller :as toasts.c]
            [tape.guis7.app.guis.crud.model :as crud.m]))

;;; Routes

(def ^::c/routes routes
  ["/crud" {:coercion rcs/coercion}
   ["" ::index]
   ["/new" {:name ::new :conflicting true}]
   ["/:id" {:conflicting true, :parameters {:path {:id int?}}}
    ["/edit" ::edit]]])

;;; Handlers

(defn ^::c/event-db index [db _]
  (cond-> (dissoc db ::v/current)
          (nil? (::people db)) (assoc ::people crud.m/seed)))

(defn ^::c/event-db new [db _]
  (-> db
      (assoc ::person {})
      (dissoc ::v/current)))

(defn ^::c/event-db edit [db [_ params]]
  (-> db
      (assoc ::person (get-in db [::people (-> params :path :id)]))
      (dissoc ::v/current)))

(defn ^::c/event-fx create [{:keys [db]} _]
  {:db         (update- db ::people crud.m/insert (::person db))
   :dispatch-n [[::router/navigate [::index]]
                [::toasts.c/create :success "Person created"]]})

(defn ^::c/event-fx update [{:keys [db]} [_ params]]
  {:db         (update- db ::people assoc (-> params :path :id) (::person db))
   :dispatch-n [[::router/navigate [::index]]
                [::toasts.c/create :success "Person updated"]]})

(defn ^::c/event-fx delete [{:keys [db]} [_ args]]
  {:db       (update- db ::people dissoc (-> args :id))
   :dispatch [::toasts.c/create :success "Person deleted"]})

(defn ^::c/event-db field [db [_ k v]] (assoc-in db [::person k] v))

;;; Sub

(defn ^::c/sub people [db _] (::people db))
(defn ^::c/sub person [db _] (::person db))

;;; Module

(c/defmodule)
