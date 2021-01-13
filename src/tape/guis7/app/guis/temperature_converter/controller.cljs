(ns tape.guis7.app.guis.temperature-converter.controller
  (:require [reitit.coercion.spec :as rcs]
            [re-frame.core :as rf]
            [tape.mvc :as mvc :include-macros true]
            [tape.guis7.app.guis.temperature-converter.model
             :as temperature-converter.m]))

;;; Routes

(def ^{::mvc/reg ::mvc/routes} routes
  ["/temperature-converter" {:coercion rcs/coercion}
   ["" ::index]])

;;; Handlers

(defn index
  {::mvc/reg ::mvc/event-db}
  [_ _] {})

(def coerce-float
  (rf/->interceptor
   :id ::coerce-float
   :before (fn unwrap-before [context]
             (update-in context [:coeffects :event 1]
                        temperature-converter.m/coerce-float))))

(defn from-celsius
  {::mvc/reg ::mvc/event-db
   ::mvc/interceptors [coerce-float (rf/path [::scope])]}
  [_ [_ celsius]]
  (when celsius
    (temperature-converter.m/from-celsius celsius)))

(defn from-fahrenheit
  {::mvc/reg ::mvc/event-db
   ::mvc/interceptors [coerce-float (rf/path [::scope])]}
  [_ [_ fahrenheit]]
  (when fahrenheit
    (temperature-converter.m/from-fahrenheit fahrenheit)))

;;; Sub

(defn scope
  {::mvc/reg ::mvc/sub}
  [db _] (::scope db))

(defn celsius
  {::mvc/reg ::mvc/sub
   ::mvc/signals [:<- [::scope]]}
  [scope _] (:celsius scope))

(defn fahrenheit
  {::mvc/reg ::mvc/sub
   ::mvc/signals [:<- [::scope]]}
  [scope _] (:fahrenheit scope))

;;; Module

(mvc/defm ::module)
