(ns aichallenge.matrix
  (:require [aichallenge.util :as u]))

(defn assoc-matrix [{:keys [mad]
                     :as matrix}
                    r c val]
  (assoc-in matrix [:data (mad r c)] val))

(defn get-matrix [{:keys [mad]
                   :as matrix}
                  r c]
  (get-in matrix [:data (mad r c)]))

(defn matrix-of [rows cols val]
  {:mad #(+ %2 (* %1 cols))
   :cols cols
   :rows rows
   :data (into [] (repeat (* rows cols) val))})

(defn matrix-size
  [m]
  {:rows (:rows m)
   :cols (:cols m)})

(defn matrix-data
  [m]
  (:data m))

(defn pr-matrix [{:keys [mad cols rows data] :as matrix} pr-val]
  (doseq [r (range rows)]
    (doseq [c (range cols)]
      (u/perr (pr-val (get-matrix matrix r c))
            \space))
    (u/perr \newline))
  (u/perr \newline))

(defn clone-matrix
  [original-matrix fill-value]
  (let [mad (:mad original-matrix)
        cols (:cols original-matrix)
        rows (:rows original-matrix)
        data (:data original-matrix)]
    {:mad mad
     :cols cols
     :rows rows
     :data (into [] (map (constantly fill-value) data))}))

(defn map-matrix!
  "For corresponding values in tm and fm, apply map-function and store result in tm"
  [tm fm map-function]
  (let
      [target (:data tm)  ; TODO check if both have same dimentions
       mix-in (:data fm)]
    (assoc tm :data (into [] (map (fn [t-item m-item](map-function t-item m-item)) target mix-in)))))

(defn matrix-count
  "Returns number of elements that are equal to test"
  ([m]
     (count (:data m)))
  ([m test]
     (let [data (:data m)]
       (count (filter #(= %1 test) data)))))
