(defproject makemake "0.1.0-SNAPSHOT"
  :description "Create makefiles for a C project"
  :url "http://github.com/aaparella/makemake"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot makemake.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
