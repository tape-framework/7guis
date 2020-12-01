(ns guis7.app.guis.circle-drawer.controller
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc.controller :as c :include-macros true]))

;;; Routes

(def routes
  ["/circle-drawer" {:coercion rcs/coercion}
   ["" ::index]])

;;; Index

(defn ^::c/event-fx index [_ _] {:db {}})

;;; Module

(c/defmodule)
