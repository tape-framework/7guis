(ns guis7.app.guis.circle-drawer.model)

(defn insert [circles circle]
  (let [id (->> circles keys (apply max) inc)]
    (assoc circles id circle)))

(defn- distance [x1 y1 x2 y2]
  (let [dx (Math/abs (- x1 x2))
        dy (Math/abs (- y1 y2))]
    (Math/sqrt (+ (* dx dx) (* dy dy)))))

(defn select
  "Returns the key of the circle closes to the mouse, if the mouse is over it."
  [circles x y]
  (let [over?   (fn [[_ [cx cy r]]] (< (distance cx cy x y) r))
        closest (fn
                  ([] nil)
                  ([c] c)
                  ([c1 c2] (let [[_ [cx1 cy1]] c1
                                 [_ [cx2 cy2]] c2
                                 d1 (distance cx1 cy1 x y)
                                 d2 (distance cx2 cy2 x y)]
                             (if (< d1 d2) c1 c2))))]
    (->> circles
         (filter over?)
         (reduce closest nil)
         first)))

(defn- ckw [name]
  (keyword "guis7.app.guis.circle-drawer.controller" name))

(def init {(ckw :snapshots)  [{} {1 [100 100 30]}]
           (ckw :snapshot-i) 1
           (ckw :undo?)      true
           (ckw :redo)       false
           (ckw :circles)    {1 [100 100 30]}})

(defn snapshot [db]
  (let [snapshot-i (inc ((ckw :snapshot-i) db))
        circles    ((ckw :circles) db)]
    (-> db
        (assoc (ckw :undo?) true)
        (assoc (ckw :redo?) false)
        (update (ckw :snapshots) assoc snapshot-i circles)
        (update (ckw :snapshots) subvec 0 (inc snapshot-i))
        (assoc (ckw :snapshot-i) snapshot-i))))

(defn undo [db]
  (let [snapshots  ((ckw :snapshots) db)
        snapshot-i (dec ((ckw :snapshot-i) db))]
    (assoc db
      (ckw :undo?) (< 0 snapshot-i)
      (ckw :redo?) true
      (ckw :circles) (get snapshots snapshot-i)
      (ckw :snapshot-i) snapshot-i)))

(defn redo [db]
  (let [snapshots  ((ckw :snapshots) db)
        snapshot-i (inc ((ckw :snapshot-i) db))]
    (assoc db
      (ckw :undo?) true
      (ckw :redo?) (< snapshot-i (dec (count snapshots)))
      (ckw :circles) (get snapshots snapshot-i)
      (ckw :snapshot-i) snapshot-i)))
