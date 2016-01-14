(ns twitter-client.twitter-stream
  (:require [mount.core :refer [defstate]]
            [twitter.oauth :as oauth]
            [twitter.api.streaming :as tas]
            [twitter-client.config :as config])
  (:import (twitter.callbacks.protocols AsyncStreamingCallback)))

(defn creds
  "Create OAuth credentials for the given config"
  [conf]
  (oauth/make-oauth-creds (:consumer-key conf)
                          (:consumer-secret conf)
                          (:user-access-token conf)
                          (:user-access-token-secret conf)))

(defn open-stream
  "Open twitter-stream"
  [bodypart-func failure-func exception-func creds]
  (let [cbs (AsyncStreamingCallback. bodypart-func failure-func exception-func)]
    (tas/statuses-filter :params {:track "BhandMeeting"}
                         :oauth-creds creds
                         :callbacks cbs)))

(defn close-stream
  "Close twitter-stream"
  [conn]
  (:cancel (meta conn)))

;; (defstate twitter-stream-conn
;;   :start 
;;   :stop (disconnect twitter-stream-conn))

