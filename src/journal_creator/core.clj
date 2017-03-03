(ns journal-creator.core
  (require [clj-time.core :as t]))

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

(defn -main
  [& args]
  (println "Today is: " (get-day-of-week 2017 03 01) (calculate-zeller-congruence 2017 03 01))
  (println (zeller-congruence 2017 02 28)))
