(ns guis7.app.guis.flight-booker.controller
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc.controller :as c :include-macros true]
            [guis7.app.guis.flight-booker.model :as flight-booker.m]))

;;; Routes

(def routes
  ["/flight-booker" {:coercion rcs/coercion}
   ["" ::index]
   ["/book" ::book]])

;;; Handlers

(defn ^::c/event-db index [_ _]
  {::booking {:kind "one"}})

(defn ^::c/event-db book [db _]
  (assoc-in db [::booking :booked] true))

(defn ^::c/event-db field
  "Set a field and recompute it's validation errors."
  [db [_ k v]]
  (let [[booking errors] (flight-booker.m/add-field (::booking db) (::booking-errors db) k v)]
    (assoc db
      ::booking booking
      ::booking-errors errors)))

;;; Sub

(defn ^::c/sub booking [db _] (::booking db))
(defn ^::c/sub booking-errors [db _] (::booking-errors db))

;;; Module

(c/defmodule)
