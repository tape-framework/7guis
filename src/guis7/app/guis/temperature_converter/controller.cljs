(ns guis7.app.guis.temperature-converter.controller
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc.controller :as c :include-macros true]
            [guis7.app.guis.temperature-converter.model :as temperature-converter.m]
            [clojure.string :as string]))

;;; Routes

(def routes
  ["/temperature-converter" {:coercion rcs/coercion}
   ["" ::index]])

;;; Handlers

(defn ^::c/event-db index [_ _] {})

(defn ^::c/event-db from-celsius [db [_ celsius]]
  (merge db
         {::celsius   celsius
          ::fahrenheit (if (string/blank? celsius)
                        ""
                        (temperature-converter.m/celsius->fahrenheit (js/parseFloat celsius)))}))

(defn ^::c/event-db from-fahrenheit [db [_ fahrenheit]]
  (merge db
         {::fahrenheit fahrenheit
          ::celsius   (if (string/blank? fahrenheit)
                        ""
                        (temperature-converter.m/fahrenheit->celsius (js/parseFloat fahrenheit)))}))

;;; Sub

(defn ^::c/sub celsius [db _] (::celsius db))
(defn ^::c/sub fahrenheit [db _] (::fahrenheit db))

;;; Module

(c/defmodule)
