(ns aichallenge.strategy
  (:use [aichallenge.matrix :as m])
  (:use [aichallenge.util :as u]))

(defn init-strategy
  [knowledge]
  (let [default-cost 0]
    (assoc knowledge :strategy-data {:cost-map (m/clone-matrix (:visit-data knowledge) default-cost)})))

(defn- need-to-explore?
  [knowledge]
  (let
        [target-exploration 0.5
         turn (:turn (:state knowledge))
         data (:visit-data knowledge)
         unknown (m/matrix-count data turn)
         total (m/matrix-count data)]
    ;(< (/ unknown total) target-exploration)
    true))

(defn- explore-strategy
  "Given the cost map, apply exploration adjustment"
  [knowledge]
  (let
      [m (:startegy-data knowledge)]
    knowledge))

(defn update-strategy
  [knowledge]
  (if (need-to-explore? knowledge)
    (explore-strategy knowledge)
    knowledge))

