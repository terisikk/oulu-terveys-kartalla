(ns terveys-backend.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [terveys-backend.handler :refer :all]))

(deftest test-app
  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
