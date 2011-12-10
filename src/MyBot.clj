(ns MyBot
  (:require
   (aichallenge [ant :as ant]
                [bot :as bot])))

(ant/start-game bot/init-bot)
