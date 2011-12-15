tools/playgame.py \
--player_seed 42 \
--end_wait=0.25 \
--verbose \
--log_dir tools/game_logs \
--turns 1000 \
--map_file tools/maps/maze/maze_04p_01.map \
-e \
--strict \
--capture_errors \
--loadtime=3000 \
--turntime=500 \
"$@" "python tools/sample_bots/python/HunterBot.py" "python tools/sample_bots/python/LeftyBot.py" "python tools/sample_bots/python/GreedyBot.py" "java -cp `lein classpath` clojure.main @MyBot.clj" \
-So | java -jar tools/visualizer.jar
