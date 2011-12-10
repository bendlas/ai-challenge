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
  (into [] (repeat (* rows cols) val)))

(defn pr-matrix [{:keys [mad cols rows data] :as matrix} pr-val]
  (doseq [r (range rows)]
    (doseq [c (range cols)]
      (u/perr (pr-val (get-matrix matrix r c))
            \space))
    (u/perr \newline))
  (u/perr \newline))
