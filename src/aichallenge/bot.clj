(ns aichallenge.bot
  (:require
   (aichallenge [ant :as ant]
                [matrix :as m]
                [fow :as fow]
                [collisions :as cs]
                [strategy :as s]
                [util :as u])))

(defn- move
  [offset]
  (cond
   (= offset [1 0] :souht)
   (= offset [0 1] :west)
   (= offset [-1 0] :north)
   (= offset [0 -1] :east)))

(defn dir-for-ant
  [ant knowledge]
  (let [moves (:assigned-moves knowledge)]
    (if (nil? (contains? moves ant))
      (first (filter #(ant/valid-move? (:state knowledge) ant %)
                     (shuffle [:north :east :west :south])))
      (u/perrln moves))))

(defn moves-for-step
  "Based on current knowledge, return next move for every ant"
  [knowledge]
  (let [new-knowledge (s/update-strategy knowledge)]
    (for [ant (:ants (:state knowledge))
          :let [dir (dir-for-ant ant new-knowledge)]
          :when dir]
      [ant dir])))

(defn init-bot [{:keys [rows cols viewradius2]}]
  (let [visible-positions (fow/visibility-pattern viewradius2 rows cols)]
    {:initial-knowledge {:visual-memory (m/matrix-of rows cols 0)}    
     :bot (fn pull-moves [knowledge]
            {:knowledge (fow/update-visibilities knowledge (mapcat visible-positions
                                                                   (ant/my-ants (:state knowledge))))
             :moves (-> (moves-for-step knowledge)
                        cs/fix-collisions)})}))


