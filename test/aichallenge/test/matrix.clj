(ns aichallenge.test.matrix
  (:use [aichallenge.matrix])
  (:use [clojure.test]))

(deftest maxtix-basics
  (let [m (matrix-of 10 10 42)]
    (is (= 42 (get-matrix m 2 2)) "Got worng value from original matrix")
    (is (= 24 (get-matrix (assoc-matrix m 3 3 24) 3 3)) "Matrix did not store the value")))

(deftest matrix-cloning
  (let [original (matrix-of 10 10 42)
        new (clone-matrix original 24)]
    (is (= (matrix-size original) (matrix-size new)) "Clonned matrix has wrong number of elements")
    (is (reduce #(and %1 (= 24 %2)) true (matrix-data new)) "Not all elemenst of clonned matrix equal 24")

    (is (= 24 (get-matrix new 2 2)) "Cannot read from clonned matrix")
    (is (= 33 (get-matrix (assoc-matrix new 2 2 33) 2 2)) "Clonned matrix cannot store value")
))

(deftest matrix-mapping
  (let [original (matrix-of 10 10 41)
        mix-in (matrix-of 10 10 1)
        expected (matrix-of 10 10 42)]
    (is (= (matrix-data (map-matrix! original mix-in #(+ %1 %2))) (matrix-data expected))) "Cannot combine two matrixes (sum failed)"))

(deftest matrix-count-test
  (let [m (matrix-of 10 10 0)]
    (is (= 100 (matrix-count m)))
    (is (= 100 (matrix-count (assoc-matrix m 1 1 42))))

    (is (= 100 (matrix-count m 0)))
    (is (= 99 (matrix-count (assoc-matrix m 1 1 42) 0)))))
