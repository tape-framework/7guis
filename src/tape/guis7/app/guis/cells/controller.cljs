(ns tape.guis7.app.guis.cells.controller
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc :as mvc :include-macros true]
            [tape.guis7.app.guis.cells.model :as cells.m]))

;;; Routes

(def ^{::mvc/reg ::mvc/routes} routes
  ["/cells" {:coercion rcs/coercion}
   ["" ::index]])

;;; Handlers

(defn index
  {::mvc/reg ::mvc/event-db}
  [_ _]
  {::cells (cells.m/make-data 5 5)})

(defn set-formula
  {::mvc/reg ::mvc/event-db}
  [db [_ i j in-formula]]
  (assoc-in db [::cells [i j] :in-formula] in-formula))

(defn recompute
  {::mvc/reg ::mvc/event-db}
  [db [_ i j]]
  (let [data (atom (::cells db))]
    (cells.m/edit data i j)
    (assoc db ::cells @data)))

;;; Sub

(defn cell
  {::mvc/reg ::mvc/sub}
  [db [_ i j]]
  (get-in db [::cells [i j]]))

;;; Module

(mvc/defm ::module)
