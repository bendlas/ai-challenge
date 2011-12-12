(ns aichallenge.bot
  (:require
   (aichallenge [ant :as ant]
                [matrix :as m]
                [fow :as fow]
                [collisions :as cs]
                [strategy :as s]
                [util :as u])))

(defn dir-for-ant
  [ant knowledge]
  (first (filter #(ant/valid-move? (:state knowledge) ant %)
                 (shuffle [:north :east :west :south]))))

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
    {:initial-knowledge (-> {:data (m/matrix-of rows cols 0)}
                            (s/init-strategy))
     :bot (fn pull-moves [knowledge]
            ;(m/pr-matrix (:cost-map (:strategy-data knowledge)) #(if
                                        ;(zero? %) \# \space))
            ;(.flush *err*)
            {:knowledge (fow/update-visibilities knowledge (mapcat visible-positions
                                                                   (ant/my-ants (:state knowledge))))
             :moves (-> (moves-for-step knowledge)
                        cs/fix-collisions)})}))


