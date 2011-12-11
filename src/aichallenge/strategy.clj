(ns aichallenge.strategy)

(defn init-strategy
  [knowledge]
  (assoc knowledge :strategy-data {}))

(defn update-strategy
  [knowledge]
  ; Nothing smart at this ponit, just return knowlesge back
  knowledge)
