(ns tape.guis7.app.guis.circle-drawer.controller
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc :as mvc :include-macros true]
            [tape.guis7.app.guis.circle-drawer.model :as circle-drawer.m]))

;;; Routes

(def ^{::mvc/reg ::mvc/routes} routes
  ["/circle-drawer" {:coercion rcs/coercion}
   ["" ::index]])

;;; Handlers

(defn index
  {::mvc/reg ::mvc/event-db}
  [_ _] circle-drawer.m/init)

(defn create
  {::mvc/reg ::mvc/event-fx}
  [{:keys [db]} [_ x y]]
  {:db (circle-drawer.m/snapshot (update db ::circles circle-drawer.m/insert [x y 30]))
   :dispatch [::select x y]})

(defn select
  {::mvc/reg ::mvc/event-db}
  [db [_ x y]]
  (assoc db ::selected (circle-drawer.m/select (::circles db) x y)))

(defn activate
  {::mvc/reg ::mvc/event-db}
  [db _]
  (assoc db ::activated? true))

(defn deactivate
  {::mvc/reg ::mvc/event-db}
  [db _]
  (assoc db ::activated? false))

(defn edit
  {::mvc/reg ::mvc/event-db}
  [db _]
  (assoc db ::activated? false
            ::editing? true))

(defn stop-edit
  {::mvc/reg ::mvc/event-db}
  [db _]
  (let [{::keys [set-radius?]} db]
    (cond-> (assoc db ::editing? false)
            set-radius? (-> (assoc ::set-radius? false)
                            circle-drawer.m/snapshot))))

(defn set-radius
  {::mvc/reg ::mvc/event-db}
  [db [_ r]]
  (-> db
      (assoc ::set-radius? true)
      (assoc-in [::circles (::selected db) 2] r)))

(defn undo
  {::mvc/reg ::mvc/event-db}
  [db _] (circle-drawer.m/undo db))

(defn redo
  {::mvc/reg ::mvc/event-db}
  [db _] (circle-drawer.m/redo db))

;;; Sub

(defn circles
  {::mvc/reg ::mvc/sub}
  [db _] (::circles db))

(defn selected
  {::mvc/reg ::mvc/sub}
  [db _] (::selected db))

(defn activated?
  {::mvc/reg ::mvc/sub}
  [db _] (::activated? db))

(defn editing?
  {::mvc/reg ::mvc/sub}
  [db _] (::editing? db))

(defn undo?
  {::mvc/reg ::mvc/sub}
  [db _] (::undo? db))

(defn redo?
  {::mvc/reg ::mvc/sub}
  [db _] (::redo? db))

;;; Module

(mvc/defm ::module)
