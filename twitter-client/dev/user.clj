(ns user
  (:require [clojure.pprint :refer [pprint]]
            [clojure.tools.namespace.repl :as tn]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [clojure.core.async :refer [chan >!! <!! >! <! go go-loop]]
            [clojure.repl :as r]
            [mount.core :as mount :refer [defstate]]
            [http.async.client :as ac]
            [twitter-client.config :as config]
            [twitter-client.twitter-stream :as ts]
            [twitter.callbacks.handlers :as tch]
            [twitter.oauth :as oauth]))

(defstate dev-twitter-api-conf
  :start (config/load-config "dev-resources/my-twitter-api-conf.edn"))

(defn start []
  (mount/start-with {#'twitter-client.config/twitter-api-conf #'dev-twitter-api-conf})
  :ready)

(defn stop []
  (mount/stop))

(defn refresh []
  (stop)
  (tn/refresh))

(defn refresh-all []
  (stop)
  (tn/refresh-all))

(defn reset []
  "stops all states defined by defstate, reload modified source files, and restarts the states"
  (stop)
  (tn/refresh :after 'user/start))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn test-stream
  []
  (let [bodypart-func (ts/bodypart-handler ts/chunk-in-chan)
        failure-func (comp println tch/response-return-everything)
        exception-func tch/exception-print
        creds (ts/creds config/twitter-api-conf)
        response (ts/open-stream bodypart-func failure-func exception-func creds)]
    (go-loop []
      (let [t (<! ts/chunk-in-chan)]
        (pprint t)
        (recur)))
    response))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(mount/in-clj-mode)
