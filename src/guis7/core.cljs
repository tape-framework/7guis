(ns guis7.core
  (:require
   [goog.dom]
   [integrant.core :as ig]
   [reagent.core :as r]
   [reagent.dom :as dom]
   [re-frame.core :as rf]
   [tape.module :as module :include-macros true]
   [tape.mvc]
   [tape.router :as router]
   [tape.tools.timeouts.controller]
   [guis7.app.layouts.view :as layouts.v]
   [guis7.app.home.controller :as home.c]
   [guis7.app.home.view]
   [guis7.app.guis.counter.controller :as counter.c]
   [guis7.app.guis.counter.view]
   [guis7.app.guis.temperature-converter.controller :as temperature-converter.c]
   [guis7.app.guis.temperature-converter.view]
   [guis7.app.guis.flight-booker.controller :as flight-booker.c]
   [guis7.app.guis.flight-booker.view]
   [guis7.app.guis.timer.controller :as timer.c]
   [guis7.app.guis.timer.view]
   [guis7.app.guis.crud.controller :as crud.c]
   [guis7.app.guis.crud.view]
   [guis7.app.guis.circle-drawer.controller :as circle-drawer.c]
   [guis7.app.guis.circle-drawer.view]
   [guis7.app.guis.cells.controller :as cells.c]
   [guis7.app.guis.cells.view]))

(module/load-hierarchy)

;;; System

(def config
  (merge (module/read-config "guis7/config.edn")
         {:tape.profile/base {::router/routes (merge home.c/routes
                                                     counter.c/routes
                                                     temperature-converter.c/routes
                                                     flight-booker.c/routes
                                                     timer.c/routes
                                                     crud.c/routes
                                                     circle-drawer.c/routes
                                                     cells.c/routes)
                              :tape/router    {:routes  (ig/ref ::router/routes)
                                               :options {:conflicts nil}}}}))

(defonce prepped-config nil)
(defonce system nil)

(defn mount []
  (when-let [el (goog.dom/getElement "app")]
    (dom/render [layouts.v/app] el)))

(defn init []
  (set! prepped-config (module/prep-config config))
  (set! system (ig/init prepped-config [:tape/main :tape/multi]))
  (rf/dispatch-sync [::router/navigate [::home.c/index]])
  (mount))

(defn halt []
  (when system (ig/halt! system)))

;;; Entry point

(defn setup []
  (doto js/window
    (.addEventListener "load" init)
    (.addEventListener "unload" halt)))
