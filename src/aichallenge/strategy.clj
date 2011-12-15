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
        offset [(- (first ant) (first desired-move)) (- (last ant) (last desired-move))]]
    (assign-move ant offset knowledge)))

(defn- gatherer-strategy
  [knowledge]
  (let [food (:food (:state knowledge))
        ants (:free-ants knowledge)
        target (first ants)
        water (:water (:state knowledge))
        vm (:visual-memory knowledge)
        acceptable? (fn [x] (and (not (contains? water x))
                                (= (m/get-matrix vm (first x) (last x)) 0)))]
    (if (or (empty? food) (nil? target))
      knowledge
      (loop [options #{ (first food) } 
             processed #{}
             new-knowledge knowledge]
        (if (empty? options)
          new-knowledge ; this ant cannot get to this food
          (let [cur (best-option options target)]
            
            (if (= cur target)
              (update-knowledge-with-move target processed new-knowledge)
              (recur (filter #(not (= cur %1)) (concat options (filter #(acceptable? %1) (expand cur))))
                     (conj processed cur)
                     new-knowledge))))))))

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

