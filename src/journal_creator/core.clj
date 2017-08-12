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

(defn get-today []
  (t/to-time-zone (t/now) (t/default-time-zone)))

(defn parse-date [date format]
  (f/unparse (f/formatter-local format) date))

(defn get-journal-header-str [date]
  (-> 
    date
    #(parse-date % "MMMM dd, yyyy, EEEE") 
    #(str "= " % " - <SUMMARY GOES HERE> =")))

(defn get-filename-extension-str [date]
  (-> 
    date
    #(parse-date "yyyy-MM-dd" %)
    #(str % ".wiki")))

(defn save [day] 
  (let [filename (get-filename-extension-str day)
        header (get-journal-header-str day)] 
    (spit filename header)))

(defn iterate-times [times]
  (let [current-date (get-today)]
  (map #(save %) 
       (time-range current-date
                   (t/plus current-date (t/days times)) (t/days 1)))))

(defn -main [& args]
  (prn "args: " args)
  (iterate-times (read-string (first args)))) 
