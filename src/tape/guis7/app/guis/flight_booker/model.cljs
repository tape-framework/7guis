(ns tape.guis7.app.guis.flight-booker.model
  (:require [clojure.string :as string]
            [validateur.validation :as validation]
            [cljs-time.format :as format]))

(defn- is-date-string [s]
  (if (string/blank? s)
    true
    (try
      (format/parse (:date format/formatters) s)
      true
      (catch js/Error e
        false))))

(def validate
  (validation/validation-set
   (validation/presence-of :kind)
   (validation/inclusion-of :kind :in #{"one" "return"})
   (validation/presence-of :leave)
   (validation/validate-by :leave is-date-string)
   (validation/validate-when #(= (:kind %) "return")
                             (validation/presence-of :return))
   (validation/validate-by :return is-date-string)))
