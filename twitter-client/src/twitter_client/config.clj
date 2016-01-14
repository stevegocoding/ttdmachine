(ns twitter-client.config
  (:require [clojure.edn :as edn]
            [mount.core :refer [defstate]]))

(defn load-config [path]
  (-> path
      slurp
      edn/read-string))

;; Twitter-API config
(defstate twitter-api-conf
  :start (load-config "resources/twitter-api-conf.edn"))

;; Redis config
;; (defstate redis-conf :start (load-config "resources/redis-conf.edn"))
