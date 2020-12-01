(ns guis7.app.guis.counter.controller
  (:refer-clojure :rename {count clojure-count  })
  (:require [reitit.coercion.spec :as rcs]
            [tape.router :as router]
            [tape.mvc.controller :as c :include-macros true]))

;;; Routes

(def routes
  ["/counter" {:coercion rcs/coercion}
   ["" ::index]
   ["/show" ::show]
   ["/increment" ::increment]])

;;; Index

(defn ^::c/event-fx index
  "Initialize counter."
  [_ _]
  {:db {::count 0}
   :dispatch [::router/navigate [::show]]})

(defn ^::c/event-db show
  "Show counter."
  [db _] db)

(defn ^::c/event-fx increment
  "Increment count."
  [{:keys [db]} _]
  {:db (update db ::count inc)
   :dispatch [::router/navigate [::show]]})

;;; Sub

(defn ^::c/sub count [db _] (::count db))

;;; Module

(c/defmodule)
