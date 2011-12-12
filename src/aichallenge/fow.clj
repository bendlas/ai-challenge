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

(defn- populate-visibility [m positions]
  (for [p positions]
    (m/assoc-matrix m (first p) (last p) true)))

(defn update-visibilities
  [matrix positions]
  (let [visible-fields (populate-visibility
                        (m/clone-matrix matrix false)
                        positions)]
   (m/map-matrix! matrix visible-fields #(if %2 0 (inc %1)))))
