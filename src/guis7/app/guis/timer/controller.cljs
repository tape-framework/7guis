(ns guis7.app.guis.timer.controller
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc.controller :as c :include-macros true]))

;;; Routes

(def routes
  ["/timer" {:coercion rcs/coercion}
   ["" ::index]])

;;; Index

(defn ^::c/event-fx index [_ _] {:db {}})

;;; Module

(c/defmodule)
