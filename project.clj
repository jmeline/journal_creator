(defproject journal_creator "0.1.0-SNAPSHOT"
  :description "Creates journal headers for vimwiki"
  :url "github.com/jmeline/journal_creator"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-time "0.13.0"]]
  :main ^:skip-aot journal-creator.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
