(defproject terveys-backend "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [clj-http "3.12.3"] 
                 [hickory "0.7.1"]
                 [org.clojure/data.json "2.4.0"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler terveys-backend.handler/app} 
  :uberjar-name "terveys-backend.jar"
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
