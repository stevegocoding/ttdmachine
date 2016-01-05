(ns user
  (:require
   [clojure.pprint :refer [pprint]]
   [clojure.tools.namespace.repl :refer [refresh refresh-all]]
   [com.stuartsierra.component :as component]
   [twitter-client.app :as app]))

(def system nil)

(def system-configs
  {:tc {:a 0 :b 1}})

(defn init []
  (alter-var-root #'system (constantly (app/make-system system-configs))))

(defn start []
  (alter-var-root #'system app/start-system))

(defn stop []
  (alter-var-root #'system
                  (fn [s] (when s (app/stop-system s)))))

(defn go []
  (init)
  (start))
