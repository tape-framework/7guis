(ns guis7.app.guis.crud.view
  (:require [tape.mvc.view :as v :include-macros true]
            [guis7.app.guis.crud.controller]))

;;; Views

(defn ^::v/view index []
  [:p "CRUD"])

;;; Module

(v/defmodule guis7.app.guis.crud.controller)
