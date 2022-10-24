(ns terveys.app
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [hickory.core :as hcore]
            [hickory.select :as hselect])
  (:require-macros [cljs.core.async.macros :refer [go]]))


(defn inner-html [element]
  (first (:content (first element))))

(defn parse-html [html] 
  (println  (inner-html (hselect/select (hselect/id :CurNumberVal) (hcore/as-hickory (hcore/parse html))))))

(defn mock-remote-call []
  (parse-html (str "<div id='CurNumberVal'>10</div>")))


(defn make-remote-call [endpoint]
  (go (let [response (<! (http/get endpoint))]
        (parse-html response))))


(defn init []
  (if goog.DEBUG
    (mock-remote-call)
    (make-remote-call "https://www.oukapalvelut.fi/ottewq/jsonview.aspx?TP=TUIRAAK")))
