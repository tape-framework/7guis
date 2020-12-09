(ns guis7.app.guis.temperature-converter.controller
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc.controller :as c :include-macros true]
            [guis7.app.guis.temperature-converter.model :as temperature-converter.m]))

;;; Routes

(def routes
  ["/temperature-converter" {:coercion rcs/coercion}
   ["" ::index]])

;;; Handlers

(defn ^::c/event-db index [_ _] {})

(defn ^::c/event-db from-celsius [db [_ celsius]]
  (assoc db
    ::celsius celsius
    ::fahrenheit (temperature-converter.m/->fahrenheit celsius)))

(defn ^::c/event-db from-fahrenheit [db [_ fahrenheit]]
  (assoc db
    ::fahrenheit fahrenheit
    ::celsius (temperature-converter.m/->celsius fahrenheit)))

;;; Sub

(defn ^::c/sub celsius [db _] (::celsius db))
(defn ^::c/sub fahrenheit [db _] (::fahrenheit db))

;;; Module

(c/defmodule)
