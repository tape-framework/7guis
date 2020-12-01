(ns guis7.app.guis.temperature-converter.model)

(defn celsius->farenheit [celsius]
  (-> celsius
      (* (/ 9 5))
      (+ 32)))

(defn farenheit->celsius [farenheit]
  (-> farenheit
      (- 32)
      (* (/ 5 9))))
