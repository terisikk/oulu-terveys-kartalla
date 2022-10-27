(ns terveys-backend.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            ;;[ring.middleware.cors :refer [wrap-cors]]
            [clj-http.client :as http]
            [hickory.core :as hcore]
            [hickory.select :as hselect]
            [ring.util.response :as response]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]))


(defn inner-html [element]
  (first (:content (first element))))

(defn find-value-by-html-id [id, sitemap]
   (inner-html (hselect/select (hselect/id id) sitemap)))

(defn parse-html-to-json [html]
  (let [sitemap (hcore/as-hickory (hcore/parse html))]
    {:curnumber (find-value-by-html-id :CurNumberVal sitemap)
     :curqueue (find-value-by-html-id :CurQueueVal sitemap)
     :queuetime (find-value-by-html-id :TimeLeftVal sitemap)
     :name (find-value-by-html-id :VastaanOttoNimiLabel sitemap)}))

(defn get-healthcenter-queue [service]
  (parse-html-to-json (:body (http/get (str "https://www.oukapalvelut.fi/ottewq/jsonview.aspx?TP=" service)))))

(defroutes app-routes
  (GET "/jono" [palvelu] (response/response (get-healthcenter-queue palvelu)))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-json-body {:keywords? true})
      (wrap-json-response)
      ;;(wrap-cors :access-control-allow-origin [#".*"] :access-control-allow-methods [:get]) ; For local development
      (wrap-defaults api-defaults)))
