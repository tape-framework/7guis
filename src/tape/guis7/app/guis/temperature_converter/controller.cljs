(ns tape.guis7.app.guis.temperature-converter.controller
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc.controller :as c :include-macros true]
            [tape.guis7.app.guis.temperature-converter.model :as temperature-converter.m]))

;;; Routes

(def ^{::c/reg ::c/routes} routes
  ["/temperature-converter" {:coercion rcs/coercion}
   ["" ::index]])

;;; Handlers

(defn index
  {::c/reg ::c/event-db}
  [_ _] {})

(defn from-celsius
  {::c/reg ::c/event-db}
  [db [_ celsius]]
  (assoc db
    ::celsius celsius
    ::fahrenheit (temperature-converter.m/->fahrenheit celsius)))

(defn from-fahrenheit
  {::c/reg ::c/event-db}
  [db [_ fahrenheit]]
  (assoc db
    ::fahrenheit fahrenheit
    ::celsius (temperature-converter.m/->celsius fahrenheit)))

;;; Sub

(defn celsius
  {::c/reg ::c/sub}
  [db _] (::celsius db))

(defn fahrenheit
  {::c/reg ::c/sub}
  [db _] (::fahrenheit db))

;;; Module

(c/defmodule)
