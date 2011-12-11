(ns aichallenge.test.matrix
  (:use [aichallenge.matrix])
  (:use [clojure.test]))

(deftest matrix-cloning
  (let [original (matrix-of 10 10 42)
        new (clone-matrix original 24)]
    (is (= (count original) (count new)) "Clonned matrix has wrong number of elements")
    (is (reduce #(and %1 (= 24 %2)) true new) "Not all elemenst of clonned matrix equal 24")
))
