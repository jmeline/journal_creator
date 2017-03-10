(ns journal-creator.core
  (require [clj-time.core     :as t]
           [clj-time.local    :as l]
           [clj-time.periodic :as p]
           [clj-time.format   :as f]))

(def days {0 "Saturday"
           1 "Sunday"
           2 "Monday" 
           3 "Tuesday" 
           4 "Wednesday"
           5 "Thursday"
           6 "Friday"})

(defn compare-jan-or-feb [x] (or (= x 01) (= x 02)))
(defn convert-month 
  [m] 
  (if (compare-jan-or-feb m) 
    (+ m 12) 
    m))

(defn convert-year [m y] (if (> m 12) (dec y) y))

(defn zeller-congruence [year month day]
  (let [J (quot year 100)
        K (mod year 100)]
    (mod (+ day
            (quot (* 13 (inc month)) 5)
            K
            (quot K 4)
            (quot J 4)
            (* 5 J))
         7)))

(defn calculate-zeller-congruence 
  [year month day]
  (let [m (convert-month month)
        y (convert-year m year)
        d day]
    (zeller-congruence y m d)))

(defn get-day-of-week [y m d]
  (get days (zeller-congruence y m d) "Unknown"))

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
 
(defn -main
  [& args]
  (println "Today is: " (get-journal-header (get-today)))
  (println "file format is: " (get-filename-extension (get-today)))
  (loop [days (number-of-days-in-month 2017 03)] 
    (when (seq days)
      (let [day (first days)
            journal-header (get-journal-header day)
            filename (get-filename-extension day)]
        (prn "remaining:" (count days) "journal-header: " journal-header)
        (prn "filename: " filename)
        (spit filename journal-header)
        (recur (rest days))))))

