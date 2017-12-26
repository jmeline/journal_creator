(defproject journal_creator "0.1.0"
  :description "Creates journal headers for vimwiki"
  :url "github.com/jmeline/journal_creator"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [clj-time "0.14.2"]]
  :main ^:skip-aot journal_creator.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
