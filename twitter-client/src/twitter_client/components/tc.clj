(ns twitter-client.components.tc
  (:require [clojure.core.async :refer [chan go-loop >!! <!]]
            [com.stuartsierra.component :as component]
            [twitter.api.streaming :as tas]
            [twitter.oauth :as oauth]
            [twitter.callbacks.handlers :as tch]))

(defrecord TwitterClient [config]
  component/Lifecycle
  (start [component]
    (println "TwitterClient - start")
    component)

  (stop [component]
    (println "TwitterClient - stop")
    component))

(defn make-tc-component
  [config]
  (map->TwitterClient config))
