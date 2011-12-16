(ns aichallenge.ant
  "Library to interact with the game"
  (:require [clojure.string :as string]
            [aichallenge.util :as u]))

;;****************************************************************
;; Constants and lookups
;;****************************************************************

(declare ^{:dynamic true} *game-info*)

(def init-state {:turn 0
                 :water #{}
                 :dead #{}
                 :enemies #{}
                 :ants #{}
                 :food #{}
                 :my-hills #{}
                 :enemy-hills #{}})

(def dir-sym {:north "N"
              :south "S"
              :east "E"
              :west "W"})

(def dir-offset {:north [-1 0]
                 :west [0 -1]
                 :south [1 0]
                 :east [0 1]})

(def offset-dir {[-1 0] :north
                 [0 -1] :west
                 [1 0] :south
                 [0 1] :east})

(def messages {:ready #"ready"
               :turn #"turn [0-9]+"
               :end #"end"
               :go #"go"
               :tile #"\w \d+ \d+"})

(def map-tiles {"f" :food 
                "w" :water 
                "a" :ant 
                "d" :dead-ant
                "h" :hill})

;;****************************************************************
;; Implementation functions
;;****************************************************************

(defn- parse-tile [msg]
  (let [[tile row col player :as parts] (string/split (string/lower-case msg) #" ")
        player (when player
                     (Integer. player))]
    {:tile (map-tiles tile)
     :row (Integer. row)
     :col (Integer. col)
     :player player}))

(defn- message? [msg-type msg]
  (re-seq (messages msg-type) (string/lower-case msg)))

(defn- build-game-info []
  (loop [cur (read-line)
         info {}]
    (if (message? :ready cur)
      info
      (let [[k v] (string/split cur #" ")
            neue (assoc info (keyword k) (Long/parseLong v))]
        (recur (read-line) neue)))))

(defn- get-turn [msg]
  (Integer. (or (second (string/split msg #" ")) 0)))

(defn- update-tile [state {:keys [tile row col player]}]
  (let [loc [row col]
        ant (conj loc player)
        me? (fn [x] (zero? x))]
        (condp = tile
          :water (update-in state [:water] conj loc)
          :dead-ant (update-in state [:dead] conj ant)
          :ant (if (me? player)
                 (update-in state [:ants] conj loc) 
                 (update-in state [:enemies] conj ant))
          :food (update-in state [:food] conj loc)
          :hill (if (me? player)
                  (update-in state [:my-hills] conj loc)
                  (update-in state [:enemy-hills] conj loc)))))

(defn- update-state [state msg]
  (cond
    (message? :turn msg) (merge init-state {:turn (get-turn msg) 
                                            :water (or (:water state) #{})})
    (message? :tile msg) (update-tile state (parse-tile msg))
    :else state))

(defn- contains-ant? [ants cur]
  (some #(let [[r c p] %]
             (= [r c] cur))
          ants))

(defn move-ant 
  "Return the location defined by moving the given ant in the given
  direction."
  [ant dir]
  (let [dir-vector (dir-offset dir)
        rows (*game-info* :rows)
        cols (*game-info* :cols)
        [r c] (map + ant dir-vector)]
    (u/perrln "Ant: " ant " Dir: " dir)
    [(cond 
       (< r 0) (+ rows r) 
       (>= r rows) (- r rows)
       :else r)
     (cond 
       (< c 0) (+ cols c) 
       (>= c cols) (- c cols)
       :else c)]))


;;****************************************************************
;; Public functions
;;****************************************************************

;; TODO: if you want to collect the information and do something
;; with it at the end, do so here. Look at build-game-info as an
;; example
(defn- collect-stats []
  )

(defn game-info
  "Get some value from the setup information of the game"
  [k]
  (*game-info* k))

(defn turn-num
  "Get the turn number"
  [gs]
  (:turn gs))

(defn my-ants 
  "Get a set of all ants belonging to you"
  [gs]
  (:ants gs))

(defn enemy-ants 
  "Get a set of all enemy ants where an enemy ant is [row col player-num]"
  [gs]
  (:enemies gs))

(defn food 
  "Get a set of food locations"
  [gs]
  (:food gs))

(defn apply-move
  "Get projected game state after move"
  [gs [[row col :as ant] dir]]
  (update-in gs [:ants]
             #(-> (dissoc % [row col])
                  (assoc % (move-ant ant dir)))))

(defn unit-distance 
  "Get the vector distance between two points on a torus. Negative deltas are 
  preserved."
  [loc loc2]
  (let [[dx dy] (map - loc2 loc)
        [adx ady] (map #(Math/abs %) [dx dy])
        [adx2 ady2] (map #(- (game-info %) %2) [:rows :cols] [adx ady])
        fx (if (<= adx adx2)
             dx
             (* adx2 (/ (- dx) adx)))
        fy (if (<= ady ady2)
             dy
             (* ady2 (/ (- dy) ady)))]
    [fx fy]))

(defn distance 
  "Get the euclidean distance between two locations on a torus"
  [loc loc2]
  (let [[dx dy] (unit-distance loc loc2)]
    (Math/sqrt (+ (Math/pow dx 2) (Math/pow dy 2)))))

(defn unoccupied? 
  "If the given location does not contain an ant or food, return loc"
  [gs loc]
  (when (and (not (contains-ant? (food gs) loc)) 
             (not (contains-ant? (my-ants gs) loc))
             (not (contains-ant? (enemy-ants gs) loc)))
    loc))

(defn passable? 
  "Deteremine if the given location can be moved to. If so, loc is returned."
  [gs loc]
  (when (and (not (contains? (gs :water) loc))
             (unoccupied? gs loc))
    loc))

(defn valid-move? 
  "Check if moving an ant in the given direction is passable. If so,
  return the location that the ant would then be in."
  [gs ant dir]
  (passable? gs (move-ant ant dir)))


(defn direction [loc loc2]
  "Determine the directions needed to move to reach a specific location.
  This does not attempt to avoid water. The result will be a collection
  containing up to two directions."
  (let [[dr dc] (unit-distance loc loc2)
        row (if-not (zero? dr)
            (/ dr (Math/abs dr))
            dr)
        col (if-not (zero? dc)
            (/ dc (Math/abs dc))
            dc)]
    (filter #(not (nil? %))
            [(offset-dir [row 0])
             (offset-dir [0 col])])))

(defn emit!
  "Emit a list of moves where each move is [ant dir], where each ant is [row col]
and dir is [:north :south :east :west]"
  [moves]
  (doseq [[[row col :as ant] dir] moves]
    (println "o" row col (dir-sym dir))))

(defn start-game 
  "Play the game with the given bot."
  [init-bot]
  (when (message? :turn (read-line))
    (binding [*game-info* (build-game-info)]
      (let [{bot :bot
             initial-knowledge :initial-knowledge} (init-bot *game-info*)]
        (println "go") ;; we're "setup" so let's start
        (loop [cur (read-line)
               bot-knowledge (assoc initial-knowledge :state {})]
          (cond
           (message? :end cur) (collect-stats)
           (message? :go cur) (let [{moves :moves
                                     new-knowledge :knowledge} (bot bot-knowledge)]
                                (emit! moves)
                                (println "go")
                                (recur (read-line)
                                       new-knowledge))
           :else (recur (read-line)
                        (assoc bot-knowledge :state (update-state (:state bot-knowledge) cur)))))))))

