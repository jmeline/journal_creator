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

(defn month-seq 
  [start end step]
  (time-range start end step))

(defn number-of-days-in-month
  [year month]
  (let [current-date (t/date-time year month)
        end-date     (t/plus current-date (t/months 1))]
    (month-seq current-date end-date (t/days 1))))

(defn get-today [] 
  (t/to-time-zone (t/now) (t/default-time-zone))) 

(defn unparse-today [formatter]
  (f/unparse formatter (get-today)))

(defn custom-formatter [format] 
  (f/formatter-local format))

(defn journal-header-format [date] 
  (str "= " date " - <SUMMARY GOES HERE> ="))

(defn file-format [file]
  (str file ".wiki"))

(def journal-format "MMMM dd, yyyy, EEEE")
(defn convert-to-journal-header [date]
  (f/unparse (custom-formatter journal-format) date))

(def filename-date-format "yyyy-MM-dd")
(defn convert-to-filename-extension [date]
  (f/unparse (custom-formatter filename-date-format) date))

(defn get-journal-header [date]
  (journal-header-format (convert-to-journal-header date)))

(defn get-filename-extension [date]
  (file-format (convert-to-filename-extension date)))

;; incrediblepoems.com - heaven wont be heaven

(defn generate-journal-files-for-month [year month] 
  (loop [days (number-of-days-in-month year month)] 
    (when (seq days)
      (let [day (first days)
            journal-header (get-journal-header day)
            filename (get-filename-extension day)]
        (prn "remaining:" (count days) "journal-header: " journal-header)
        (prn "filename: " filename)
        (spit filename journal-header)
        (recur (rest days))))))

(defn -main
  [& args]
  (println "Today is: " (get-journal-header (get-today)))
  (println "file format is: " (get-filename-extension (get-today)))
  (generate-journal-files 2017 03))
