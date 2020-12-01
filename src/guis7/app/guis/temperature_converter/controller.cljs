(ns guis7.app.guis.temperature-converter.controller
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc.controller :as c :include-macros true]
            [tape.mvc.view :as v :include-macros true]
            [guis7.app.guis.temperature-converter.model :as temperature-converter.m]
            [clojure.string :as string]))

;;; Routes

(def routes
  ["/temperature-converter" {:coercion rcs/coercion}
   ["" ::index]])

;;; Index

(defn ^::c/event-db index [_ _] {})

(defn ^::c/event-db from-celsius [_ [_ celsius]]
  {::celsius   celsius
   ::farenheit (if (string/blank? celsius)
                 ""
                 (temperature-converter.m/celsius->farenheit (js/parseFloat celsius)))
   ::v/current ::index})

(defn ^::c/event-db from-farenheit [_ [_ farenheit]]
  {::farenheit farenheit
   ::celsius (if (string/blank? farenheit)
               ""
               (temperature-converter.m/farenheit->celsius (js/parseFloat farenheit)))
   ::v/current ::index})

;;; Sub

(defn ^::c/sub celsius [db _] (::celsius db))
(defn ^::c/sub farenheit [db _] (::farenheit db))

;;; Module

(c/defmodule)
