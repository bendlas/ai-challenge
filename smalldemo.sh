tools/playgame.py \
--player_seed 42 \
--end_wait=0.25 \
--verbose \
--log_dir tools/game_logs \
--turns 1000 \
--map_file tools/maps/mymap.map \
--food sections \
-e \
--strict \
--capture_errors \
--loadtime=5000 \
--turntime=500 \
"$@" "java -cp `lein classpath` clojure.main @MyBot.clj" "python tools/sample_bots/python/LeftyBot.py" \
-So | java -jar tools/visualizer.jar
