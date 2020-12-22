(ns tape.guis7.core
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
   [tape.guis7.app.layouts.app :as app]))

(mvc/require-modules "src/tape/guis7/app")

(module/load-hierarchy)

(defmethod ig/init-key ::main [_ _]
  (dom/render [app/app] (goog.dom/getElement "app")))

;;; System

(def config
  (merge (module/read-config "tape/guis7/config.edn")
         (mvc/modules-map "src/tape/guis7/app")))
