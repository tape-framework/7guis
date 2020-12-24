(ns tape.guis7.app.guis.flight-booker.controller
  (:require [reitit.coercion.spec :as rcs]
            [tape.mvc.controller :as c :include-macros true]
            [tape.guis7.app.guis.flight-booker.model :as flight-booker.m]))

;;; Routes

(def ^{::c/reg ::c/routes} routes
  ["/flight-booker" {:coercion rcs/coercion}
   ["" ::index]
   ["/book" ::book]])

;;; Helpers

(defn- add-field
  "A setter of sorts, adds the k-v to the map and updates errors and derivatives. Lives in the controller because it
  gives a controller-centric view of the model that mirrors what we show in the view (the errors part)."
  [booking k v]
  (let [errors   (:errors booking)
        booking' (assoc booking k v)
        errors'  (flight-booker.m/validate booking')]
    (-> booking'
        (assoc
          :valid (empty? errors')
          ;; we want to affect errors only on the field being edited
          :errors (assoc errors k (get errors' k)))
        ;; clean `leave` when disabled
        (cond-> (and (= k :kind) (= v "one"))
                (-> (dissoc :return)
                    (update :errors dissoc :return))))))

;;; Handlers

(defn index
  {::c/reg ::c/event-db}
  [_ _]
  {::booking {:kind "one"}})

(defn book
  {::c/reg ::c/event-db}
  [db _]
  (assoc-in db [::booking :booked] true))

(defn field
  "Set a field and recompute it's validation errors."
  {::c/reg ::c/event-db}
  [db [_ k v]]
  (assoc db ::booking (add-field (::booking db) k v)))

;;; Sub

(defn booking
  {::c/reg ::c/sub}
  [db _] (::booking db))

;;; Module

(c/defmodule)
