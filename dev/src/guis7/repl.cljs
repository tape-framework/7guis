(ns ^:figwheel-hooks guis7.repl
  (:require [integrant.core :as ig]
            [re-frame.db]
            [tape.module :as module]
            [guis7.core :as guis7]))

(defn ^:after-load on-reload []
  (swap! re-frame.db/app-db update :__figwheel_counter inc)
  (when guis7/system
    (ig/halt! guis7/system)
    (set! guis7/system (-> guis7/config module/prep-config ig/init)))
  (guis7/mount))

(guis7/setup)
