(ns tape.guis7.app.guis.timer.controller
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc.controller :as c :include-macros true]
            [tape.mvc.view :as v :include-macros true]
            [tape.router :as router]
            [tape.tools.timeouts.controller :as timeouts.c]))

;;; Routes

(def ^{::c/reg ::c/routes} routes
  ["/timer" {:coercion rcs/coercion}
   ["" ::index]
   ["/reset" ::reset]])

;;; Helpers

(def ^:private init {::timer {:progress 0, :duration 50}})
(def ^:private timeout {:ms 100, :set [::timeout-set] :timeout [::tick]})

;;; Handlers

(defn index
  {::c/reg ::c/event-fx}
  [{:keys [db]} _]
  (let [timeout-id (-> db ::timer :timeout-id)
        events     (cond-> [[::timeouts.c/set timeout]]
                           (some? timeout-id) (conj [::timeouts.c/clear timeout-id]))]
    {:db         init
     :dispatch-n events}))

(defn timeout-set
  {::c/reg ::c/event-db}
  [db [_ timeout-id]]
  (assoc-in db [::timer :timeout-id] timeout-id))

(defn tick
  {::c/reg ::c/event-fx}
  [{:keys [db]} _]
  (let [{:keys [progress duration]} (::timer db)
        {::v/keys [current]} db
        more? (and (= current ::index)
                   (< progress duration))]
    (cond-> {:db db}
            more? (-> (update-in [:db ::timer :progress] + 0.1)
                      (assoc :dispatch [::timeouts.c/set timeout])))))

(defn field
  {::c/reg ::c/event-db}
  [db [_ k v]] (assoc-in db [::timer k] v))

;;; Sub

(defn timer
  {::c/reg ::c/sub}
  [db _] (::timer db))

;;; Module

(c/defmodule)
