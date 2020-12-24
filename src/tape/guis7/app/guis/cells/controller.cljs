(ns tape.guis7.app.guis.cells.controller
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc.controller :as c :include-macros true]
            [tape.guis7.app.guis.cells.model :as cells.m]))

;;; Routes

(def ^{::c/reg ::c/routes} routes
  ["/cells" {:coercion rcs/coercion}
   ["" ::index]])

;;; Handlers

(defn index
  {::c/reg ::c/event-db}
  [_ _]
  {::cells (cells.m/make-data 5 5)})

(defn set-formula
  {::c/reg ::c/event-db}
  [db [_ i j in-formula]]
  (assoc-in db [::cells [i j] :in-formula] in-formula))

(defn recompute
  {::c/reg ::c/event-db}
  [db [_ i j]]
  (let [data (atom (::cells db))]
    (cells.m/edit data i j)
    (assoc db ::cells @data)))

;;; Sub

(defn cell
  {::c/reg ::c/sub}
  [db [_ i j]]
  (get-in db [::cells [i j]]))

;;; Module

(c/defmodule)
