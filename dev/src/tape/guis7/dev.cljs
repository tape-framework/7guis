(ns ^:figwheel-hooks tape.guis7.dev
  (:require [re-frame.core :as rf]
            [re-frame.db]
            [integrant.repl :as repl]
            [tape.module :as module :include-macros true]
            [tape.router :as router]
            [tape.guis7.app.home.controller :as home.c]
            [tape.guis7.core :as guis7]))

(def profiles [:tape.profile/dev :tape.profile/local])

(repl/set-prep! #(module/prep-config guis7/config profiles))

(defn go []
  (repl/go [::guis7/main :tape/multi]))

(defn ^:after-load on-reload []
  (swap! re-frame.db/app-db update :__figwheel_counter inc)
  (repl/reset))

(defonce init
  (do (go)
      (rf/dispatch-sync [::router/navigate [::home.c/index]])))
