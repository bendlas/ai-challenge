(ns MyBot
  (:require
   (aichallenge [ant :as ant]
                [bot :as bot])))

(.println *err* "Here we are")
(ant/start-game bot/init-bot)
