(ns guis7.app.guis.timer.view
  (:require [tape.mvc.view :as v :include-macros true]
            [guis7.app.guis.counter.controller]))

;;; Views

(defn ^::v/view index []
  [:p "Timer"])

;;; Module

(v/defmodule guis7.app.guis.timer.controller)
