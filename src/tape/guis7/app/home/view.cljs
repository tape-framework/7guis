(ns tape.guis7.app.home.view
  (:require [tape.mvc.view :as v :include-macros true]
            [tape.guis7.app.home.controller]))

;;; Views

(defn ^::v/view index []
  [:p
   "7GUIs based on "
   [:a {:href "https://eugenkiss.github.io/7guis/"} "https://eugenkiss.github.io/7guis/"]
   "."])

;;; Module

(v/defmodule)
