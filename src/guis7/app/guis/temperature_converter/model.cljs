(ns guis7.app.guis.temperature-converter.model)

(defn celsius->fahrenheit [celsius]
  (-> celsius
      (* (/ 9 5))
      (+ 32)))

(defn fahrenheit->celsius [fahrenheit]
  (-> fahrenheit
      (- 32)
      (* (/ 5 9))))
