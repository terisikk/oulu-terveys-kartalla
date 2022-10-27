(ns terveys.app
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [dommy.core :as dommy :refer-macros [sel1]]
            [hipo.core :as hipo])

  (:require-macros [cljs.core.async.macros :refer [go]]))


(defn mock-remote-call []
  {:name "Tuira akuuttivastaanotto" :curnumber "AI018" :curqueue "9" :queuetime "57 min"})

(defn make-remote-call [endpoint]
  (:body (go (<! (http/get endpoint)))))

(defn healthcare-json-to-html [json]
  (js/console.log json)
  (hipo/create
   [:table
    [:tr
     [:td [:div {:class "centertitle"} (:name json)]]
     [:td [:img {:src "images/ticket.png" :class "icon"}] [:span (:curnumber json)]]
     [:td [:img {:src "images/queue.png" :class "icon"}] [:span (:curqueue json)]]
     [:td [:img {:src "images/time.png" :class "icon"}] [:span (:queuetime json)]]]]))

(defn get-healthcenter-json [center]
  (if goog.DEBUG
    (mock-remote-call)
    (make-remote-call (str "https://terveys.teemurisikko.com/api/jono?palvelu=" center))))

(defn render-healthcenter-data [center]
  (dommy/append! (sel1 :#healthcenter) (healthcare-json-to-html (get-healthcenter-json center))))

(defn init [] 
  (doall (map render-healthcenter-data ["TUIRAAK" "KAAKKURIAK"])))

