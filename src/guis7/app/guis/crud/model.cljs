(ns guis7.app.guis.crud.model)

(def seed
  {1 {:id 1 :first-name "John" :last-name "Doe"}
   2 {:id 2 :first-name "Alexandre" :last-name "Dumas"}
   3 {:id 3 :first-name "Garcia" :last-name "Marquez"}})

(defn insert [people person]
  (let [id (->> people keys (apply max) inc)
        person' (assoc person :id id)]
    (assoc people id person')))
