(ns journal-creator.core-test
  (:require [clojure.test :refer :all]
            [journal-creator.core :refer :all]
            [clj-time.core :as t]))

(deftest verify-utility-functions
  (testing "test get-journal-header return correct headers"
    (is (= (get-journal-header (t/date-time 2017 03 18))
           "= March 18, 2017, Saturday - <SUMMARY GOES HERE> =" ))
    (is (= (get-journal-header (t/date-time 2017 03))
           "= March 01, 2017, Wednesday - <SUMMARY GOES HERE> =" ))
    (is (= (get-journal-header (t/date-time 2017 12 18))
           "= December 18, 2017, Monday - <SUMMARY GOES HERE> =" )))

  (testing "test get-filename-extension returns correct format"
    (is (= (get-filename-extension (t/date-time 2017 3 18))  "2017-03-18.wiki"))
    (is (= (get-filename-extension (t/date-time 2014 2 8))   "2014-02-08.wiki"))
    (is (= (get-filename-extension (t/date-time 2027 1 1))   "2027-01-01.wiki"))
    (is (= (get-filename-extension (t/date-time 2017 12 14)) "2017-12-14.wiki")))

  (testing "test days-in-month returns correct count of days in the month"
    (is (= (count (days-in-month 2017 1)) 31))
    (is (= (count (days-in-month 2017 2)) 28))
    (is (= (count (days-in-month 2017 3)) 31))
    (is (= (count (days-in-month 2017 4)) 30))
    (is (= (count (days-in-month 2017 5)) 31))
    (is (= (count (days-in-month 2017 6)) 30))
    (is (= (count (days-in-month 2017 7)) 31))
    (is (= (count (days-in-month 2017 8)) 31))
    (is (= (count (days-in-month 2017 9)) 30))
    (is (= (count (days-in-month 2017 10)) 31))
    (is (= (count (days-in-month 2017 11)) 30))
    (is (= (count (days-in-month 2017 12)) 31))
))

