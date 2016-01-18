(defproject twitter-client "0.1.0-SNAPSHOT"
  :description "Twitter streaming API client"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/core.async "0.2.374"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.slf4j/slf4j-log4j12 "1.7.13"]
                 [mount "0.1.8"]
                 [twitter-api "0.7.8"]]
  :plugins [[cider/cider-nrepl "0.11.0-SNAPSHOT"]
            [refactor-nrepl "2.0.0-SNAPSHOT"]]
  :target-path "target/%s"
  :profiles {:uberjar {:at :all}
             :dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "0.2.11"]
                                  [org.clojure/java.classpath "0.2.3"]]}})
