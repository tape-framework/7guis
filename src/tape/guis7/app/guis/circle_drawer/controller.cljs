(ns tape.guis7.app.guis.circle-drawer.controller
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc.controller :as c :include-macros true]
            [tape.guis7.app.guis.circle-drawer.model :as circle-drawer.m]))

;;; Routes

(def ^{::c/reg ::c/routes} routes
  ["/circle-drawer" {:coercion rcs/coercion}
   ["" ::index]])

;;; Handlers

(defn index
  {::c/reg ::c/event-db}
  [_ _] circle-drawer.m/init)

(defn create
  {::c/reg ::c/event-fx}
  [{:keys [db]} [_ x y]]
  {:db (circle-drawer.m/snapshot (update db ::circles circle-drawer.m/insert [x y 30]))
   :dispatch [::select x y]})

(defn select
  {::c/reg ::c/event-db}
  [db [_ x y]]
  (assoc db ::selected (circle-drawer.m/select (::circles db) x y)))

(defn activate
  {::c/reg ::c/event-db}
  [db _]
  (assoc db ::activated? true))

(defn deactivate
  {::c/reg ::c/event-db}
  [db _]
  (assoc db ::activated? false))

(defn edit
  {::c/reg ::c/event-db}
  [db _]
  (assoc db ::activated? false
            ::editing? true))

(defn stop-edit
  {::c/reg ::c/event-db}
  [db _]
  (let [{::keys [set-radius?]} db]
    (cond-> (assoc db ::editing? false)
            set-radius? (-> (assoc ::set-radius? false)
                            circle-drawer.m/snapshot))))

(defn set-radius
  {::c/reg ::c/event-db}
  [db [_ r]]
  (-> db
      (assoc ::set-radius? true)
      (assoc-in [::circles (::selected db) 2] r)))

(defn undo
  {::c/reg ::c/event-db}
  [db _] (circle-drawer.m/undo db))

(defn redo
  {::c/reg ::c/event-db}
  [db _] (circle-drawer.m/redo db))

;;; Sub

(defn circles
  {::c/reg ::c/sub}
  [db _] (::circles db))

(defn selected
  {::c/reg ::c/sub}
  [db _] (::selected db))

(defn activated?
  {::c/reg ::c/sub}
  [db _] (::activated? db))

(defn editing?
  {::c/reg ::c/sub}
  [db _] (::editing? db))

(defn undo?
  {::c/reg ::c/sub}
  [db _] (::undo? db))

(defn redo?
  {::c/reg ::c/sub}
  [db _] (::redo? db))

;;; Module

(c/defmodule)
