(ns aichallenge.strategy
  (:use [aichallenge.matrix :as m])
  (:use [aichallenge.util :as u]))

(defn- assign-move
  [ant move knowledge]
  (let [free-ants (:free-ants knowledge)
        moves (:assigned-moves knowledge)]
    (assoc knowledge :free-ants (filter #(not (= %1 ant)) free-ants))
    (if (empty? moves)
      (assoc knowledge :assigned-moves {ant move})
      (assoc knowledge :assigned-moves (assoc (:assigned-moves knowledge) ant move)))))

(defn- in-danger?
  [knowledge]
  false)

(defn- warrior-strategy
  [knowledge]
  (if (in-danger? knowledge)
    knowledge ;protect hill
    knowledge))

(defn- expand
  [[r c]]
  [[(dec r) c] [r (inc c)] [(inc r) c] [r (dec c)]])

(defn- best-option
  [options target]
  (let [pow2 (fn [x] (* x x))
        distance (fn [opt] (Math/sqrt (+ (pow2 (- (first target) (first opt))) (pow2 (- (last target) (last opt))))))]
    (reduce #(cond
              (nil? %2) %1
              (<= (distance %1) (distance %2)) %1
              :esle %2) options)))

(defn- update-knowledge-with-move
  [ant expanded knowledge]
  (let [desired-move (best-option expanded ant)
        offset [(- (first desired-move) (first ant)) (- (last desired-move) (last ant))]]
    (assign-move ant offset knowledge)))

(defn- get-route
  [start target knowledge]
  (let [water (:water (:state knowledge))
        vm (:visual-memory knowledge)
        acceptable? (fn [x] (and (not (contains? water x))
                                (= (m/get-matrix vm (first x) (last x)) 0)))]
       (loop [options #{start} 
              processed #{}
              new-knowledge knowledge]
         (if (empty? options)
           new-knowledge
           (let [cur (best-option options target)]
             (if (= cur target)
               (update-knowledge-with-move target processed new-knowledge)
               (recur (filter #(not (= cur %1)) (into options (filter
                                                               #(and (acceptable? %1) (not (contains? options %1)) (not (contains? processed %1)))
                                                               (expand cur))))
                      (conj processed cur)
                      new-knowledge)))))))

(defn- gatherer-strategy
  [knowledge]
  (loop [food (:food (:state knowledge))
         ants (:free-ants knowledge)
         knowledge knowledge]
    (if (or (empty? food) (empty? ants))
      knowledge
      (let [target (first ants)
            start (best-option food target)]
        (recur (filter #(not (= start %1)) food)
               (filter #(not (= target %1)) ants)
               (if (or (nil? start) (nil? target))
                 knowledge
                 (get-route start target knowledge)))))))

(defn- explorer-strategy
  "Given the cost map, apply exploration adjustment"
  [knowledge]
  knowledge)

(defn update-strategy
  [knowledge]
  (let [new-knowledge (assoc knowledge :free-ants (:ants (:state knowledge)))]
    (->
     (warrior-strategy new-knowledge)
     (gatherer-strategy)
     (explorer-strategy))))

