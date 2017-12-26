(ns journal_creator.core
  (:require [clojure.java.io   :as io]
            [clj-time.core     :as t]
            [clj-time.periodic :as p]
            [clj-time.format   :as f])
  (:gen-class))

(defn time-range
  [start end step]
  (let [inf-range (p/periodic-seq start step)
        below-end? #(t/within? (t/interval start end) %)]
    (take-while below-end? inf-range)))

(defn get-today []
  (t/to-time-zone (t/now) (t/default-time-zone)))

(defn parse-date-generator [date]
  (fn [format] (f/unparse (f/formatter-local format) date)))

(defn get-journal-header [date-parser]
  (-> "MMMM dd, yyyy, EEEE"
      date-parser
      (#(str "= " % " - <SUMMARY GOES HERE> ="))))

(defn get-file-extension [date-parser]
  (->  "yyyy-MM-dd"
       date-parser
       (#(str % ".wiki"))))

(defn save [day]
  (let [date-parser-function (parse-date-generator day)
        filename (get-file-extension date-parser-function)
        header (get-journal-header date-parser-function)]
    (when (not (.exists (io/file filename)))
      (spit filename header))))

(defn iterate-times [date-list]
  (doseq [day date-list]
    (save day)))

(defn parse-value [value]
  (read-string (clojure.string/replace value #"[+-]" "")))

(defn get-date-modify-fn [arg]
  (let [value (re-find #"[+-]" arg)
        times (t/days (parse-value arg))
        current-date (get-today)]
    (if (= "-" value)
      #(% (t/minus current-date times) current-date (t/days 1))
      #(% current-date (t/plus current-date times) (t/days 1)))))

(defn -main [& args]
  (when (> (count args) 0)
    (iterate-times
      ((get-date-modify-fn (first args)) time-range)))
  (println "Done!"))
