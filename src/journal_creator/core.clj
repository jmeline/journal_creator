(ns journal-creator.core
  (require [clj-time.core     :as t]
           [clj-time.local    :as l]
           [clj-time.periodic :as p]
           [clj-time.format   :as f]))

(defn time-range
  [start end step]
  (let [inf-range (p/periodic-seq start step)
        below-end? (fn [t] (t/within? (t/interval start end) t))]
    (take-while below-end? inf-range)))

(defn days-in-month
  ([year month] (days-in-month year month 01))
  ([year month day]
   (let [current-date (t/date-time year month day)
         end-date     (t/plus current-date (t/months 1))]
     (time-range current-date end-date (t/days 1)))))

(defn get-today []
  (t/to-time-zone (t/now) (t/default-time-zone)))

(defn unparse-today [formatter]
  (f/unparse formatter (get-today)))

(defn journal-header-format [date]
  (str "= " date " - <SUMMARY GOES HERE> ="))

(defn file-format [file]
  (str file ".wiki"))

(defn convert-to-journal-header [date]
  (f/unparse (f/formatter-local "MMMM dd, yyyy, EEEE") date))

(defn convert-to-filename-extension [date]
  (f/unparse (f/formatter-local "yyyy-MM-dd") date))

(defn get-journal-header-str [day]
  ((comp journal-header-format convert-to-journal-header) day))

(defn get-filename-extension-str [day]
  ((comp file-format convert-to-filename-extension) day))

;; incrediblepoems.com - heaven wont be heaven

(defn generate-journal-files
  ([year month] (generate-journal-files year month 01))
  ([year month d]
   (loop [days (days-in-month year month d)]
     (when (seq days)
       (let [day (first days)
             journal-header (get-journal-header-str day)
             filename (get-filename-extension-str day)]
         (prn "remaining:" (count days) "journal-header: " journal-header "filename: " filename)
         (spit filename journal-header)
         (recur (rest days)))))))

(defn -main [& args]
  ;; (println "Today is: " (get-journal-header-str (get-today)))
  ;; (println "file format is: " (get-filename-extension-str (get-today)))
  (prn "args: " args)
  (generate-journal-files 2017 07 04))
