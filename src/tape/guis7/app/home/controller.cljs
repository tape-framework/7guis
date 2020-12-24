(ns tape.guis7.app.home.controller
  (:refer-clojure :rename {update update-})
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc.controller :as c :include-macros true]))

;;; Routes

(def ^{::c/reg ::c/routes} routes
  ["" {:coercion rcs/coercion}
   ["/" ::index]])

;;; Index

(defn index
  {::c/reg ::c/event-db}
  [_ _] {})

;;; Module

(c/defmodule)
