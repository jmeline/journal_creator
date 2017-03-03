(ns journal-creator.core-test
  (:require [clojure.test :refer :all]
            [journal-creator.core :refer :all]))

(deftest verify-day
  (testing "converting january and february to 13 and 14 respectively"
    (is (= 13 (convert-month 01)))
    (is (= 14 (convert-month 02)))
    (is (= 03 (convert-month 03))))

  (testing "Given a date, return a day of the week."
    (is (= "Monday"  (get-day-of-week 2014 3 3)))
    (is (= "Tuesday" (get-day-of-week 2011 8 2)))
    (is (= "Wednesday"(get-day-of-week 2017 3 1)))
    (is (= "Thursday" (get-day-of-week 2015 2 1)))
    (is (= "Friday" (get-day-of-week 2013 3 29)))
    (is (= "Sunday" (get-day-of-week 2012 3 18)))
    (is (= "Saturday" (get-day-of-week 2017 23 1)))
    (is (= "Sunday" (get-day-of-week 2015 4 26)))
    (is (= "Sunday" (get-day-of-week 2010 3 21)))))
