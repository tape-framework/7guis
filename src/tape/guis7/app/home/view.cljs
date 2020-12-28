(ns tape.guis7.app.home.view
  (:require [tape.mvc :as mvc :include-macros true]
            [tape.guis7.app.home.controller]))

;;; Views

(defn index
  {::mvc/reg ::mvc/view}
  []
  [:p
   "7GUIs based on "
   [:a {:href "https://eugenkiss.github.io/7guis/"} "https://eugenkiss.github.io/7guis/"]
   "."])

;;; Module

(mvc/defm ::module)
