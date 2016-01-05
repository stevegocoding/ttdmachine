(ns twitter-client.app
  (:require [com.stuartsierra.component :as component]
            [twitter-client.components.tc :as tc]))

(defn make-system
  [config-options]
  (component/system-map
   :tc (tc/make-tc-component (:tc config-options))))

(defn start-system
  [system]
  (component/start system))

(defn stop-system
  [system]
  (component/stop system))
