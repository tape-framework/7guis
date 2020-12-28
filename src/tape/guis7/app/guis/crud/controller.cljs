(ns tape.guis7.app.guis.crud.controller
  "People CRUD persisted to localstorage via datascript."
  {:tape.mvc/interceptors [datascript.c/inject]}
  (:refer-clojure :rename {list list-})
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc :as mvc :include-macros true]
            [tape.router :as router]
            [tape.tools.current.controller :as current.c]
            [tape.toasts.controller :as toasts.c]
            [tape.datascript.controller :as datascript.c]
            [tape.guis7.app.guis.crud.model :as model.m]))

;;; Routes

(def ^{::mvc/reg ::mvc/routes} routes
  ["/crud" {:coercion rcs/coercion}
   ["" ::index]
   ["/list" ::list]
   ["/new" {:name ::new :conflicting true}]
   ["/:id" {:conflicting true, :parameters {:path {:id int?}}}
    ["/edit" ::edit]]])

;;; Handlers

(defn index
  "Seed and redirect to list."
  {::mvc/reg ::mvc/event-fx}
  [{::datascript.c/keys [ds]} _]
  {:fx [[::datascript.c/ds (model.m/maybe-seed ds)]
        [:dispatch [::router/navigate [::list]]]]})

(defn list
  {::mvc/reg ::mvc/event-fx}
  [{::datascript.c/keys [ds] :keys [db]} _]
  {:db (-> db
           (assoc ::current.c/view ::index)
           (assoc ::people (model.m/all ds)))
   ::datascript.c/dump true})

(defn new
  {::mvc/reg ::mvc/event-db}
  [db _]
  (-> db
      (dissoc ::current.c/view)
      (assoc ::person {})))

(defn edit
  {::mvc/reg ::mvc/event-fx}
  [{::datascript.c/keys [ds] :keys [db]} [_ params]]
  (let [id (-> params :path :id)]
    {:db (-> db
             (dissoc ::current.c/view)
             (assoc ::person (model.m/one ds id)))}))

(defn save
  {::mvc/reg ::mvc/event-fx}
  [{::datascript.c/keys [ds] :keys [db]} _]
  {::datascript.c/ds (model.m/save ds (::person db))
   :dispatch-n [[::router/navigate [::list]]
                [::toasts.c/create :success "Person saved"]]})

(defn delete
  {::mvc/reg ::mvc/event-fx}
  [{::datascript.c/keys [ds]} [_ args]]
  (let [id (:id args)]
    {::datascript.c/ds (model.m/delete ds id)
     :dispatch-n [[::router/navigate [::list]]
                  [::toasts.c/create :success "Person deleted"]]}))

(defn field
  {::mvc/reg ::mvc/event-db}
  [db [_ k v]]
  (assoc-in db [::person k] v))

;;; Sub

(defn people
  {::mvc/reg ::mvc/sub}
  [db _] (::people db))

(defn person
  {::mvc/reg ::mvc/sub}
  [db _] (::person db))

;;; Module

(mvc/defm ::module)
