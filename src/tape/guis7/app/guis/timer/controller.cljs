(ns tape.guis7.app.guis.timer.controller
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc :as mvc :include-macros true]
            [tape.tools.current.controller :as current.c]
            [tape.tools.timeouts.controller :as timeouts.c]))

;;; Routes

(def ^{::mvc/reg ::mvc/routes} routes
  ["/timer" {:coercion rcs/coercion}
   ["" ::index]
   ["/reset" ::reset]])

;;; Helpers

(def ^:private init {::timer {:progress 0, :duration 50}})
(def ^:private timeout {:ms 100, :set [::timeout-set], :timeout [::tick]})

;;; Handlers

(defn index
  {::mvc/reg ::mvc/event-fx}
  [{:keys [db]} _]
  (let [timeout-id (-> db ::timer :timeout-id)
        events (cond-> [[::timeouts.c/set timeout]]
                       (some? timeout-id) (conj [::timeouts.c/clear
                                                 timeout-id]))]
    {:db init
     :dispatch-n events}))

(defn timeout-set
  {::mvc/reg ::mvc/event-db}
  [db [_ timeout-id]]
  (assoc-in db [::timer :timeout-id] timeout-id))

(defn tick
  {::mvc/reg ::mvc/event-fx}
  [{:keys [db]} _]
  (let [{:keys [progress duration]} (::timer db)
        {::current.c/keys [view]} db
        more? (and (= view ::index)
                   (< progress duration))]
    (cond-> {:db db}
            more? (-> (update-in [:db ::timer :progress] + 0.1)
                      (assoc :dispatch [::timeouts.c/set timeout])))))

(defn field
  {::mvc/reg ::mvc/event-db}
  [db [_ k v]] (assoc-in db [::timer k] v))

;;; Sub

(defn timer
  {::mvc/reg ::mvc/sub}
  [db _] (::timer db))

;;; Module

(mvc/defm ::module)
