(ns guis7.app.guis.temperature-converter.view
  (:require [tape.mvc.view :as v :include-macros true]
            [guis7.app.guis.counter.controller]))

;;; Views

(defn ^::v/view index []
  [:p "Temperature converter"])

;;; Module

(v/defmodule guis7.app.guis.temperature-converter.controller)
