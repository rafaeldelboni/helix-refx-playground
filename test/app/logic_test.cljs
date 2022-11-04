(ns app.logic-test
  (:require [clojure.test :refer [deftest is]]))

(deftest filter-by-test
  (is (= {:value 1} {:value 1})))
