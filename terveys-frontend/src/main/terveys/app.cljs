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
  {:name "Tuira akuuttivastaanotto" :curnumber "AI018" :curqueue "9" :queuetime "57 min"})

(defn append-healthcenter-data [endpoint]
  (go (let [response (<! (http/get endpoint {:with-credentials? false}))]
        (render-healthcenter-data (:body response)))))

(defn append-healthcenter-data-by-id [center]
    (append-healthcenter-data (str "http://127.0.0.1:3000/jono?palvelu=" center)))

(defn init []
  (doall (map append-healthcenter-data-by-id ["TUIRAAK" "KAAKKURIAK"])))

