(ns guis7.app.guis.temperature-converter.controller
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc.controller :as c :include-macros true]
            [guis7.app.guis.temperature-converter.model :as temperature-converter.m]
            [clojure.string :as string]))

;;; Routes

(def routes
  ["/temperature-converter" {:coercion rcs/coercion}
   ["" ::index]])

;;; Index

(defn ^::c/event-db index [_ _] {})

(defn ^::c/event-db from-celsius [db [_ celsius]]
  (merge db
         {::celsius   celsius
          ::farenheit (if (string/blank? celsius)
                        ""
                        (temperature-converter.m/celsius->farenheit (js/parseFloat celsius)))}))

(defn ^::c/event-db from-farenheit [db [_ farenheit]]
  (merge db
         {::farenheit farenheit
          ::celsius   (if (string/blank? farenheit)
                        ""
                        (temperature-converter.m/farenheit->celsius (js/parseFloat farenheit)))}))

;;; Sub

(defn ^::c/sub celsius [db _] (::celsius db))
(defn ^::c/sub farenheit [db _] (::farenheit db))

;;; Module

(c/defmodule)
