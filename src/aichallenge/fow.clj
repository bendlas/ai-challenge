(ns aichallenge.fow
  (:require [aichallenge.matrix :as m]
            [aichallenge.util :as u]))

(defn visibility-pattern [r2 rows cols]
  (let [delta (-> r2
                  Math/sqrt
                  Math/ceil
                  Math/round)
        max-r (dec rows)
        max-c (dec cols)
        delta-range (range (- delta) (inc delta))
        pattern (for [r delta-range
                      c delta-range
                      :when (<= (+ (* r r) (* c c))
                                r2)]
                  [r c])]
    (fn visible-positions [[r c]]
      (for [[pr pc] pattern]
        [(u/wrap (+ r pr) max-r)
         (u/wrap (+ c pc) max-c)]))))

(defn- populate-visibility [data mad positions]
  (reduce (fn [data [r c]]
            (assoc data (mad r c) true))
          data positions))

(defn update-visibilities [{:keys [mad data]
                            :as matrix}
                           positions]
  (let [visible-fields (populate-visibility
                        (m/clone-matrix data false)
                        mad positions)]
    (assoc matrix
      :data (into [] (map (fn [field visible]
                            (if visible
                              0
                              (inc field)))
                          data visible-fields)))))

