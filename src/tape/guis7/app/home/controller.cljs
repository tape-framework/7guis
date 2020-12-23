(ns tape.guis7.app.home.controller
  (:refer-clojure :rename {update update-})
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc.controller :as c :include-macros true]))

;;; Routes

(def ^::c/routes routes
  ["" {:coercion rcs/coercion}
   ["/" ::index]])

;;; Index

(defn ^::c/event-db index [_ _] {})

;;; Module

(c/defmodule)
