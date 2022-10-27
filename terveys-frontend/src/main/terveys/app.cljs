(ns terveys.app
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [dommy.core :as dommy :refer-macros [sel1]]
            [hipo.core :as hipo])

  (:require-macros [cljs.core.async.macros :refer [go]]))


(defn healthcare-json-to-html [json]
  (hipo/create
   [:table
    [:tr
     [:td [:div {:class "centertitle"} (:name json)]]
     [:td [:img {:src "images/ticket.png" :class "icon"}] [:span (:curnumber json)]]
     [:td [:img {:src "images/queue.png" :class "icon"}] [:span (:curqueue json)]]
     [:td [:img {:src "images/time.png" :class "icon"}] [:span (:queuetime json)]]]]))

(defn render-healthcenter-data [json]
  (dommy/append! (sel1 :#healthcenter) (healthcare-json-to-html json)))

(defn mock-remote-call [] 
  (render-healthcenter-data {:name "Tuira akuuttivastaanotto" :curnumber "AI018" :curqueue "9" :queuetime "57 min"}))

(defn make-remote-call [endpoint]
  (go (let [response (<! (http/get endpoint))]
        (render-healthcenter-data response))))

(defn get-healthcenter-json [center]
  (if goog.DEBUG
    (mock-remote-call)
    (make-remote-call (str "https://terveys.teemurisikko.com/api/jono?palvelu=" center))))

(defn init []
  (get-healthcenter-json "TUIRAAK")
  (get-healthcenter-json "KAAKKURIAK"))

