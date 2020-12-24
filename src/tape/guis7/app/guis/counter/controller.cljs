(ns tape.guis7.app.guis.counter.controller
  (:refer-clojure :rename {count count-})
  (:require [reitit.coercion.spec :as rcs]
            [tape.router :as router]
            [tape.mvc.controller :as c :include-macros true]))

;;; Routes

(def ^{::c/reg ::c/routes} routes
  ["/counter" {:coercion rcs/coercion}
   ["" ::index]
   ["/show" ::show]
   ["/increment" ::increment]])

;;; Handlers

(defn index
  "Initialize counter."
  {::c/reg ::c/event-fx}
  [_ _]
  {:db {::count 0}
   :dispatch [::router/navigate [::show]]})

(defn show
  "Show counter."
  {::c/reg ::c/event-db}
  [db _] db)

(defn increment
  "Increment count."
  {::c/reg ::c/event-fx}
  [{:keys [db]} _]
  {:db (update db ::count inc)
   :dispatch [::router/navigate [::show]]})

;;; Sub

(defn count
  {::c/reg ::c/sub}
  [db _] (::count db))

;;; Module

(c/defmodule)
