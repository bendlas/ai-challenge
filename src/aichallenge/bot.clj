(ns aichallenge.bot
  (:require [aichallenge.ant :as ant]))

(defn perr [& strs]
  (doseq [s (interpose \space strs)]
    (.print *err* s)))

(defn perrln [& strs]
  (apply perr strs)
  (perr \newline))

(defn init-bot [game-info]
  (fn pull-moves [{:keys [state knowledge]}]
    {:moves (for [ant (:ants state)
                  :let [dir (first (filter #(ant/valid-move? state ant %)
                                           [:north :east :west :south]))]
                  :when dir]
              [ant dir])
     :knowledge knowledge}))
