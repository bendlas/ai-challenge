(ns aichallenge.collisions
  (:use [clojure.set :only (difference)])
  (:require [aichallenge.ant :as ant]))

(defn- collisions [moves]
  (reduce (fn [accum move]
            (let [next-loc (apply ant/move-ant move)]
              (if (accum next-loc)
                (update-in accum [next-loc] conj move)
                (assoc accum next-loc [move]))))
          {}
          moves))

(let [directions #{:north :south :east :west}]
  (defn- alternatives
    ([collisions]
     (for [collision collisions
           :let  [moves (val collision)]
           :when (> (count moves) 1)
           [ant dir] moves]
       (first (alternatives collisions ant dir))))
    ([collisions ant dir]
     (for [other-dir (difference directions #{dir})
           :let  [next-loc (ant/move-ant ant other-dir)]
           :when (not (collisions next-loc))]
       [ant other-dir]))))

(defn fix-collisions [moves]
  (let [alts (reduce (fn [accum [ant dir]]
                       (assoc accum ant dir))
                     {}
                     (-> moves collisions alternatives))]
    (for [[ant dir] moves
          :let [dir (or (alts ant) dir)]]
          [ant dir])))         

#_(zipmap (map first moves)
          (map second moves))
