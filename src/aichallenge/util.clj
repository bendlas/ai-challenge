(ns aichallenge.util
  (:use [clojure.pprint :only (pprint)]))

(defn wrap
  ([val upper] (wrap val 0 upper))
  ([val lower upper]
     (let [l (inc (- upper lower))]
      (cond
       (< val lower) (recur (+ val l) lower upper)
       (< upper val) (recur (- val l) lower upper)
       :else val))))

(defn perr [& strs]
  (doseq [s (interpose \space strs)]
    (.print *err* s)))

(defn perrln
  ([]
     (.println *err*))
  ([& strs]
      (apply perr strs)
      (.println *err*)))

(defn fpprint [filename data]
  (spit (java.io.File. filename)
        (with-out-str (pprint data))))
