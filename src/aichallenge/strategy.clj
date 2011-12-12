(ns aichallenge.strategy
  (:use [aichallenge.matrix :as m])
  (:use [aichallenge.util :as u]))

(defn- in-danger?
  [knowledge]
  false)

(defn- warrior-strategy
  [knowledge]
  (if (in-danger? knowledge)
    knowledge ;protect hill
    knowledge))

(defn- gatherer-strategy
  [knowledge]
  (let [food (:food (:state knowledge))]
    (if (empty? food)
      knowledge
      knowledge ;there is food, lets go and get it
      )))

(defn- explorer-strategy
  "Given the cost map, apply exploration adjustment"
  [knowledge]
  knowledge)

(defn update-strategy
  [knowledge]
  (->
   (warrior-strategy knowledge)
   (gatherer-strategy)
   (explorer-strategy)))

