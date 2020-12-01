(ns guis7.app.guis.flight-booker.view
  (:require [tape.mvc.view :as v :include-macros true]
            [guis7.app.guis.flight-booker.controller]))

;;; Views

(defn ^::v/view index []
  [:p "Flight booker"])

;;; Module

(v/defmodule guis7.app.guis.flight-booker.controller)
