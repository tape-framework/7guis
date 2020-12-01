(ns guis7.app.guis.circle-drawer.view
  (:require [tape.mvc.view :as v :include-macros true]
            [guis7.app.guis.circle-drawer.controller]))

;;; Views

(defn ^::v/view index []
  [:p "Circle drawer"])

;;; Module

(v/defmodule guis7.app.guis.circle-drawer.controller)
