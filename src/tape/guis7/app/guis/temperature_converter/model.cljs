(ns tape.guis7.app.guis.temperature-converter.model
  (:require [clojure.string :as string]))

(defn- ->fahrenheit* [celsius]
  (-> celsius
      (* (/ 9 5))
      (+ 32)))

(defn- ->celsius* [fahrenheit]
  (-> fahrenheit
      (- 32)
      (* (/ 5 9))))

(defn ->fahrenheit [celsius]
  (if (string/blank? celsius)
    ""
    (->fahrenheit* (js/parseFloat celsius))))

(defn ->celsius [fahrenheit]
  (if (string/blank? fahrenheit)
    ""
    (->celsius* (js/parseFloat fahrenheit))))
