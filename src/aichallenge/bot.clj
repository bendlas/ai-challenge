(ns aichallenge.bot
  (:require
   (aichallenge [ant :as ant]
                [matrix :as m]
                [fow :as fow]
                [collisions :as cs])))

(defn init-bot [{:keys [rows cols viewradius2]}]
  (let [visible-positions (fow/visibility-pattern viewradius2 rows cols)]
    {:initial-knowledge {:mad #(+ %2 (* %1 cols))
                         :cols cols
                         :rows rows
                         :data (m/matrix-of rows cols 0)}
     :bot (fn pull-moves [{:keys [state knowledge]}]
            (m/pr-matrix knowledge #(if (zero? %) \# \space))
            (.flush *err*)
            {:knowledge (fow/update-visibilities knowledge (mapcat visible-positions
                                                                   (ant/my-ants state)))
             :moves (-> (for [ant (:ants state)
                              :let [dir (first (filter #(ant/valid-move? state ant %)
                                                       (shuffle [:north :east :west :south])))]
                              :when dir]
                          [ant dir])
                        cs/fix-collisions)})}))


