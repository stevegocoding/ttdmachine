(ns twitter-client.twitter-stream
  (:require [clojure.string :as str]
            [clojure.core.async :refer [go go-loop chan >!! <!! >! <! close!]]
            [clojure.pprint :refer [pprint]]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [mount.core :refer [defstate]]
            [twitter.api.streaming :as tas]
            [twitter-client.config :as config]
            [twitter.oauth :as oauth])
  (:import (twitter.callbacks.protocols AsyncStreamingCallback)))

(defn creds
  "Create OAuth credentials for the given config"
  [conf]
  (oauth/make-oauth-creds (:consumer-key conf)
                          (:consumer-secret conf)
                          (:user-access-token conf)
                          (:user-access-token-secret conf)))

(defn assemble-transducer
  "Returns a transducer for putting new item onto chunk channel"
  []
  (fn [rf]
    (let [partial (atom "")]
      (fn
        ([r] (rf r))
        ([r x]
         (let [partial-plus-new (str @partial x)
               parts (str/split partial-plus-new #"\r\n" -1)
               complete (subvec parts 0 (dec (count parts)))]
           (reset! partial (last parts))
           (reduce rf r complete)))))))

(defn buffer-stream-pp
  []
  (map (fn [chunk]
         (pprint (str chunk))
         (println "-------------------------------------------"))))

(defn parse-json
  [tweet]
  (try
    (json/read-json tweet)
    (catch Exception ex (log/error "Exception while parsing twitter JSON" ex "\n" tweet))))

(defn tweet?
  "Check if a data chunk is tweet"
  [data]
  (contains? data :text))

(defn log-count
  []
  (fn [rf]
    (let [cnt (atom 0)]
      (fn
        ([r] (rf r))
        ([r x]
         (swap! cnt inc)
         (when (zero? (mod @cnt 1000)) (log/info "processed" @cnt "since startup"))
         (rf r x))))))

(defn process-chunk
  []
  (comp (assemble-transducer)
        (map parse-json)
        (filter tweet?)
        (map println)))

(defn bodypart-handler
  "Returns a body part callback function."
  [chunk-chan]
  (fn [boas baos]
    (>!! chunk-chan (str baos))))

(defn open-stream
  "Open twitter-stream"
  [bodypart-func failure-func exception-func creds]
  (let [cbs (AsyncStreamingCallback. bodypart-func failure-func exception-func)]
    (tas/statuses-filter :params {:track "bigdata"}
                         :oauth-creds creds
                         :callbacks cbs)))

(defn close-stream
  "Close twitter-stream"
  [conn]
  ((:cancel (meta conn))))

(defstate chunk-in-chan
  :start (chan 1 (assemble-transducer))
  :stop (close! chunk-in-chan))
