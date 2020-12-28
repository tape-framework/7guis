(ns tape.guis7.app.home.controller
  (:refer-clojure :rename {update update-})
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc :as mvc :include-macros true]))

;;; Routes

(def ^{::mvc/reg ::mvc/routes} routes
  ["" {:coercion rcs/coercion}
   ["/" ::index]])

;;; Index

(defn index
  {::mvc/reg ::mvc/event-db}
  [_ _] {})

;;; Module

(mvc/defm ::module)
