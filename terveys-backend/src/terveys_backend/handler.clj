(ns terveys-backend.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [clj-http.client :as http]
            [hickory.core :as hcore]
            [hickory.select :as hselect]
            [clojure.data.json :as json]))


(defn inner-html [element]
  (first (:content (first element))))

(defn find-value-by-html-id [id, sitemap]
   (inner-html (hselect/select (hselect/id id) sitemap)))

(defn parse-html-to-json [html]
  (let [sitemap (hcore/as-hickory (hcore/parse html))]
    (json/write-str {:curnumber (find-value-by-html-id :CurNumberVal sitemap)
                     :curqueue (find-value-by-html-id :CurQueueVal sitemap)
                     :queuetime (find-value-by-html-id :TimeLeftVal sitemap)
                     :name (find-value-by-html-id :VastaanOttoNimiLabel sitemap)})))

(defn get-healthcenter-queue [service]
  (parse-html-to-json (:body (http/get (str "https://www.oukapalvelut.fi/ottewq/jsonview.aspx?TP=" service)))))

(defroutes app-routes
  (GET "/jono" [palvelu] (get-healthcenter-queue palvelu))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes api-defaults))
