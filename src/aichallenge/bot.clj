(ns aichallenge.bot
  (:require
   (aichallenge [ant :as ant]
                [matrix :as m]
                [fow :as fow]
                [collisions :as cs]
                [strategy :as s])))

(defn dir-for-ant
  [ant state knowledge]
  (first (filter #(ant/valid-move? state ant %)
                 (shuffle [:north :east :west :south]))))

(defn moves-for-step
  "Based on current state and accumulated knoledge, come up with moves for next step"
  [state knowledge]
  (let [new-knowledge (s/update-strategy state knowledge)]
    (for [ant (:ants state)
          :let [dir (dir-for-ant ant state new-knowledge)]
          :when dir]
      [ant dir])))

(defn init-bot [{:keys [rows cols viewradius2]}]
  (let [visible-positions (fow/visibility-pattern viewradius2 rows cols)]
    {:initial-knowledge {:mad #(+ %2 (* %1 cols))
                         :cols cols
                         :rows rows
                         :data (m/matrix-of rows cols 0)}
     :bot (fn pull-moves [{:keys [state knowledge]}]
            ;(m/pr-matrix knowledge #(if (zero? %) \# \space))
            {:knowledge (fow/update-visibilities knowledge (mapcat visible-positions
                                                                   (ant/my-ants state)))
             :moves (-> (moves-for-step state knowledge)
                        cs/fix-collisions)})}))


