(ns tape.guis7.app.guis.temperature-converter.model
  (:require [clojure.string :as string]))

(defn coerce-float [x]
  (if (string/blank? x)
    nil
    (let [y (js/parseFloat x)]
      (if (js/isNaN y) nil y))))

(defn- ->fahrenheit [celsius]
  (-> celsius
      (* (/ 9 5))
      (+ 32)))

(defn- ->celsius [fahrenheit]
  (-> fahrenheit
      (- 32)
      (* (/ 5 9))))

(defn from-celsius [celsius]
  {:celsius celsius
   :fahrenheit (->fahrenheit celsius)})

(defn from-fahrenheit [fahrenheit]
  {:fahrenheit fahrenheit
   :celsius (->celsius fahrenheit)})
