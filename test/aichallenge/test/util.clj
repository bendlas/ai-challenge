(ns aichallenge.test.util
  (:use [aichallenge.util])
  (:use [clojure.test]))

(deftest wrap-test
  (is (= 0 (wrap 0 5)))
  (is (= 1 (wrap 1 5)))
  (is (= 3 (wrap 3 5)))
  
  (is (= 5 (wrap 5 5)))
  (is (= 0 (wrap 6 5)))
  (is (= 1 (wrap 7 5)))    
  (is (= 2 (wrap 8 5)))    
  (is (= 3 (wrap 9 5)))    
  (is (= 4 (wrap 10 5)))    
  (is (= 5 (wrap 11 5)))    

  (is (= 5 (wrap -1 5)))
  (is (= 4 (wrap -2 5)))
  (is (= 3 (wrap -3 5)))
  (is (= 2 (wrap -4 5)))
  (is (= 1 (wrap -5 5)))
  (is (= 0 (wrap -6 5)))
  (is (= 5 (wrap -7 5))))
