(ns tape.guis7.app.guis.crud.model
  (:require [datascript.core :as d]))

;;; Seed

(def seeds
  [{:person/first-name "John"
    :person/last-name "Doe"}
   {:person/first-name "Alexandre"
    :person/last-name "Dumas"}
   {:person/first-name "Garcia"
    :person/last-name "Marquez"}])

(defn maybe-seed [ds]
  (letfn [(none? []
            (nil? (d/q '[:find ?e .
                         :where [?e :person/first-name]]
                       ds)))]
    (cond-> ds
            (none?) (d/db-with seeds))))

;;; CRUD

(defn all [ds]
  (d/q '[:find [(pull ?e [*]) ...]
         :where [?e :person/first-name]]
       ds))

(defn one [ds id]
  (d/q '[:find (pull ?e [*]) .
         :in $ ?e
         :where [?e :person/first-name]]
       ds id))

(defn save [ds person]
  (d/db-with ds [person]))

(defn delete [ds id]
  (d/db-with ds [[:db/retractEntity id]]))
