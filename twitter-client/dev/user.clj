(ns user
  (:require [clojure.pprint :refer [pprint]]
            [clojure.tools.namespace.repl :as tn]
            [mount.core :as mount :refer [defstate]]
            [http.async.client :as ac]
            [twitter-client.config :as config]
            [twitter-client.twitter-stream :as ts]
            [twitter.oauth :as oauth]))

(defstate dev-twitter-api-conf
  :start (config/load-config "dev-resources/my-twitter-api-conf.edn"))

(defn start []
  (mount/start-with {#'twitter-client.config/twitter-api-conf #'dev-twitter-api-conf}))

(defn stop []
  (mount/stop))

(defn refresh []
  (stop)
  (tn/refresh))

(defn refresh-all []
  (stop)
  (tn/refresh-all))

(defn go []
  "starts all states defined by defstate"
  (start)
  :ready)

(defn reset []
  "stops all states defined by defstate, reload modified source files, and restarts the states"
  (stop)
  (tn/refresh :after 'user/go))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(mount/in-clj-mode)
