(ns aichallenge.collisions
  (:use [clojure.pprint :only (pprint)]
        [clojure.set :only (difference)])
  (:require [aichallenge.ant :as ant]))

(defn init-matrix [x y]
  (letfn [(row [] (vec (take x (repeat 0))))]
    (vec (repeatedly y row))))

(defn collisions [moves]
  (loop [m {}
         ms moves]
    (if (seq ms)
      (recur (let [move (first ms)
                   next-loc (apply ant/move-ant move)]
               (if (m next-loc)
                 (update-in m [next-loc] conj move)
                 (assoc m next-loc [move])))
             (next ms))
      m)))

(def directions #{:north :south :east :west})

(defn find-alternative [m ant dirs]
  (let [poss (map (fn [ant dir]
                    [ant dir (ant/move-ant ant dir)])
                  (repeat ant)
                  dirs)]
    (first (remove (fn [[_ _ next-move]] (m next-move))
                       poss))))

(defn all-alternatives [collisions]
  (for [collision collisions
        :let  [moves (val collision)]
        :when (> (count moves) 1)
        [ant dir] moves]
    (find-alternative collisions ant (difference directions #{dir}))))

(defn fix-collisions [matrix moves]
    (let [cs   (collisions moves)
          alts (reduce (fn [m [ant dir _]]
                         (assoc m ant dir)) {}
                       (all-alternatives cs))]
      (for [[ant dir] moves]
        [ant (if-let [new-dir (alts ant)]
               new-dir
               dir)])))            

#_(zipmap (map first moves)
          (map second moves))
