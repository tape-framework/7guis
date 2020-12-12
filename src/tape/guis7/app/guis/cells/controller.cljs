(ns tape.guis7.app.guis.cells.controller
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc.controller :as c :include-macros true]
            [tape.guis7.app.guis.cells.model :as cells.m]))

;;; Routes

(def routes
  ["/cells" {:coercion rcs/coercion}
   ["" ::index]])

;;; Handlers

(defn ^::c/event-db index [_ _]
  {::cells (cells.m/make-data 5 5)})

(defn ^::c/event-db set-formula [db [_ i j in-formula]]
  (assoc-in db [::cells [i j] :in-formula] in-formula))

(defn ^::c/event-db recompute [db [_ i j]]
  (let [data (atom (::cells db))]
    (cells.m/edit data i j)
    (assoc db ::cells @data)))

;;; Sub

(defn ^::c/sub cell [db [_ i j]]
  (get-in db [::cells [i j]]))

;;; Module

(c/defmodule)
