(defproject twitter-client "0.1.0-SNAPSHOT"
  :description "Twitter streaming API client"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/core.async "0.2.374"]
                 [com.stuartsierra/component "0.3.1"]
                 [twitter-api "0.7.8"]]
  :plugins [[cider/cider-nrepl "0.11.0-SNAPSHOT"]]
  :main ^:skip-aot twitter-client.core
  :target-path "target/%s"
  :profiles {:uberjar {:at :all}
             :dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "0.2.11"]
                                  [org.clojure/java.classpath "0.2.3"]]}})
