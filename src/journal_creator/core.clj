(ns journal-creator.core
  (require [clj-time.core :as t]
           [clj-time.local :as l]
           [clj-time.format :as f]))

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

(defn unparse-today [formatter]
  (f/unparse formatter (t/to-time-zone (t/now) (t/default-time-zone))))

(defn custom-formatter [format] (f/formatter-local format))
(def get-journal-header (comp #(str "= " %1 " - <SUMMARY GOES HERE> =") unparse-today custom-formatter)) 
(def journal-format "MMMM dd, yyyy, EEEE")

(defn -main
  [& args]
  ; (println "Today is: " (get-today (custom-formatter journal-format)))
  (println "Today is: " (get-journal-header journal-format))
  (loop [i (read-line)]
    (println "row " i)
    (if (= i "bye")
      (println "All done!")
      (recur (read-line))))) 
