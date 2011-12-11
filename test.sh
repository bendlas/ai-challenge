#!/bin/sh

tools/playgame.py \
  --engine_seed 42 \
  --player_seed 42 \
  --food none \
  --end_wait=0.25 \
  --verbose \
  --log_dir tools/game_logs \
  --turns 30 \
  --map_file tools/maps/random_walk/random_walk_02p_02.map \
  "java -cp `lein classpath` clojure.main @MyBot.clj" \
  "java -cp `lein classpath` clojure.main @MyBot.clj" \
  -e \
  --strict \
  --capture_errors \
  --loadtime=10000 \
  -So | java -jar tools/visualizer.jar
#  --nolaunch
