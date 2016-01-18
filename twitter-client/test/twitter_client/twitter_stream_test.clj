(ns twitter-client.twitter-stream-test
  (:require [clojure.test :refer :all]
            [http.async.client :as ac]
            [twitter-client.config :as config]
            [twitter-client.twitter-stream :as ts]))

(def twitter-api-conf (atom nil))

(defn load-twitter-api-conf [test-fn]
  (reset! twitter-api-conf (config/load-config "dev-resources/my-twitter-api-conf.edn"))
  (test-fn))

(use-fixtures :once load-twitter-api-conf)

;;; Tests

(deftest test-open-close-stream
  (let [bodypart-func (fn [resp chunk] nil)
        failure-func (fn [resp] nil)
        exception-func (fn [resp] nil)
        creds (ts/creds @twitter-api-conf)
        response (ts/open-stream identity identity identity creds)]
    (try
      (try (is (= (:code (ac/status response)) 200))
           (finally (ts/close-stream response)))
      (catch java.util.concurrent.CancellationException e (str "Caught Exception" (.getMessage e))))))

(run-tests)
