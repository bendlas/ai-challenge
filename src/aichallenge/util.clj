(ns aichallenge.util
  (:use [clojure.pprint :only (pprint)]))

(defn wrap
  ([val upper]
     (mod val (inc upper)))
  ([val lower upper]
     (+ lower
        (wrap (- val lower)
              (- upper lower)))))

(defn perr [& strs]
  (doseq [s (interpose \space strs)]
    (.print *err* s)))

(defn perrln [& strs]
  (apply perr strs)
  (.println *err*))

(defn fpprint [filename data]
  (spit (java.io.File. filename)
        (with-out-str (pprint data))))
