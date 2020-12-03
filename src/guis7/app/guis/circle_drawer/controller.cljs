(ns guis7.app.guis.circle-drawer.controller
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc.controller :as c :include-macros true]
            [guis7.app.guis.circle-drawer.model :as circle-drawer.m]))

;;; Routes

(def routes
  ["/circle-drawer" {:coercion rcs/coercion}
   ["" ::index]])

;;; Handlers

(defn ^::c/event-db index [_ _] circle-drawer.m/init)

(defn ^::c/event-fx create [{:keys [db]} [_ x y]]
  {:db (circle-drawer.m/snapshot (update db ::circles circle-drawer.m/insert [x y 30]))
   :dispatch [::select x y]})

(defn ^::c/event-db select [db [_ x y]]
  (assoc db ::selected (circle-drawer.m/select (::circles db) x y)))

(defn ^::c/event-db activate [db _]
  (assoc db ::activated? true))

(defn ^::c/event-db deactivate [db _]
  (assoc db ::activated? false))

(defn ^::c/event-db edit [db _]
  (assoc db ::activated? false
            ::editing? true))

(defn ^::c/event-db stop-edit [db _]
  (let [{::keys [set-radius?]} db]
    (cond-> (assoc db ::editing? false)
            set-radius? (-> (assoc ::set-radius? false)
                            circle-drawer.m/snapshot))))

(defn ^::c/event-db set-radius [db [_ r]]
  (-> db
      (assoc ::set-radius? true)
      (assoc-in [::circles (::selected db) 2] r)))

(defn ^::c/event-db undo [db _] (circle-drawer.m/undo db))

(defn ^::c/event-db redo [db _] (circle-drawer.m/redo db))

;;; Sub

(defn ^::c/sub circles [db _] (::circles db))
(defn ^::c/sub selected [db _] (::selected db))
(defn ^::c/sub activated? [db _] (::activated? db))
(defn ^::c/sub editing? [db _] (::editing? db))
(defn ^::c/sub undo? [db _] (::undo? db))
(defn ^::c/sub redo? [db _] (::redo? db))

;;; Module

(c/defmodule)
