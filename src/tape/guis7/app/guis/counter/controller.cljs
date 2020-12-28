(ns tape.guis7.app.guis.counter.controller
  (:refer-clojure :rename {count count-})
  (:require [reitit.coercion.spec :as rcs]
            [tape.router :as router]
            [tape.mvc :as mvc :include-macros true]))

;;; Routes

(def ^{::mvc/reg ::mvc/routes} routes
  ["/counter" {:coercion rcs/coercion}
   ["" ::index]
   ["/show" ::show]
   ["/increment" ::increment]])

;;; Handlers

(defn index
  "Initialize counter."
  {::mvc/reg ::mvc/event-fx}
  [_ _]
  {:db {::count 0}
   :dispatch [::router/navigate [::show]]})

(defn show
  "Show counter."
  {::mvc/reg ::mvc/event-db}
  [db _] db)

(defn increment
  "Increment count."
  {::mvc/reg ::mvc/event-fx}
  [{:keys [db]} _]
  {:db (update db ::count inc)
   :dispatch [::router/navigate [::show]]})

;;; Sub

(defn count
  {::mvc/reg ::mvc/sub}
  [db _] (::count db))

;;; Module

(mvc/defm ::module)
