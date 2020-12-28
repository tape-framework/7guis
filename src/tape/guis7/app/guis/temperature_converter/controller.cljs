(ns tape.guis7.app.guis.temperature-converter.controller
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc :as mvc :include-macros true]
            [tape.guis7.app.guis.temperature-converter.model :as temperature-converter.m]))

;;; Routes

(def ^{::mvc/reg ::mvc/routes} routes
  ["/temperature-converter" {:coercion rcs/coercion}
   ["" ::index]])

;;; Handlers

(defn index
  {::mvc/reg ::mvc/event-db}
  [_ _] {})

(defn from-celsius
  {::mvc/reg ::mvc/event-db}
  [db [_ celsius]]
  (assoc db
    ::celsius celsius
    ::fahrenheit (temperature-converter.m/->fahrenheit celsius)))

(defn from-fahrenheit
  {::mvc/reg ::mvc/event-db}
  [db [_ fahrenheit]]
  (assoc db
    ::fahrenheit fahrenheit
    ::celsius (temperature-converter.m/->celsius fahrenheit)))

;;; Sub

(defn celsius
  {::mvc/reg ::mvc/sub}
  [db _] (::celsius db))

(defn fahrenheit
  {::mvc/reg ::mvc/sub}
  [db _] (::fahrenheit db))

;;; Module

(mvc/defm ::module)
