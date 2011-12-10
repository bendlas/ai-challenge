#!/bin/sh

tools/playgame.py \
  --engine_seed 42 \
  --player_seed 42 \
  --food none \
  --end_wait=0.25 \
  --verbose \
  --log_dir tools/game_logs \
  --turns 30 \
  --map_file tools/submission_test/test.map \
  "java -cp `lein classpath` clojure.main @MyBot.clj" \
  "python tools/submission_test/TestBot.py" \
  -e \
  --strict \
  --capture_errors \
  --loadtime=10000
#  --nolaunch
