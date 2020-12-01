(ns guis7.app.guis.counter.view
  (:require [tape.mvc.view :as v :include-macros true]
            [guis7.app.guis.counter.controller]))

;;; Views

(defn ^::v/view index []
  [:p "Counter"])

;;; Module

(v/defmodule guis7.app.guis.counter.controller)
