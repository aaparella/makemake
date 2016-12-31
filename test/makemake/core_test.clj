(ns makemake.core-test
  (:require [clojure.test :refer :all]
            [makemake.core :refer :all]))

(deftest get-filename-test
  (testing "Get filename from include"
    (is (= "testing" 
           (get-filename "#include \"testing.h\"")))
    (is (= "foobar"
           (get-filename "#include \"foobar.h\"")))))

(deftest get-object-test
  (testing "Get object filename from filename"
    (is (= "testing.o"
           (object-file "testing.c")))))

