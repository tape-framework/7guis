(ns guis7.app.guis.cells.view
  (:require [tape.mvc.view :as v :include-macros true]
            [guis7.app.guis.cells.controller]))

;;; Views

(defn ^::v/view index []
  [:p "Cells"])

;;; Module

(v/defmodule guis7.app.guis.cells.controller)
