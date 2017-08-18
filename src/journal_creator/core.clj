(ns journal-creator.core
  (require [clj-time.core     :as t]
           [clj-time.periodic :as p]
           [clj-time.format   :as f]))

(defn time-range
  [start end step]
  (let [inf-range (p/periodic-seq start step)
        below-end? (fn [t] (t/within? (t/interval start end) t))]
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
    (spit filename header)))

(defn iterate-times [times]
  (let [current-date (get-today)]
    (doseq [day (time-range current-date (t/plus current-date (t/days times)) (t/days 1))]
      (save day))))

(defn -main [& args]
  (iterate-times (read-string (first args))))
