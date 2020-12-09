(ns guis7.core
  (:require
   [goog.dom]
   [integrant.core :as ig]
   [reagent.dom :as dom]
   [re-frame.core :as rf]
   [tape.module :as module :include-macros true]
   [tape.mvc :as mvc :include-macros true]
   [tape.router :as router]
   [tape.tools.timeouts.controller]
   [tape.toasts.controller]
   [guis7.app.layouts.app :as app]
   [guis7.app.home.controller :as home.c]))

(mvc/require-modules "src/guis7/app")

(module/load-hierarchy)

(defmethod ig/init-key ::main [_ _]
  (rf/dispatch-sync [::router/navigate [::home.c/index]])
  (dom/render [app/app] (goog.dom/getElement "app")))

;;; System

(def ^:private modules-discovery (mvc/modules-discovery "src/guis7/app"))

(def config
  (merge (module/read-config "guis7/config.edn")
         (:modules modules-discovery)
         {:tape.profile/base {::router/routes (into [""] (:routes modules-discovery))
                              :tape/router    {:routes  (ig/ref ::router/routes)
                                               :options {:conflicts nil}}
                              ::mvc/main {:router (ig/ref :tape/router)}
                              ::main (ig/ref ::mvc/main)}}))
