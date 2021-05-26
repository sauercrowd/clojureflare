(ns clojureflare.core)

(defn route [method path fn] {:method method :path path :handler fn})

(defn make-response [resp]
  (js/Response. (:body resp)
                {"status" (get-in resp [:params :status])
                 "headers" (get-in resp [:params :headers])}))

(defn assemble-handlers [routes]
  (into {}
        (map #(vector (:path %) %) routes)))

(defmulti routefn type)
(defmethod routefn js/String [resp] {:body resp :params {:status 200}})
(defmethod routefn PersistentArrayMap [resp]
  {:body (.stringify js/JSON (clj->js resp))
  :params {:status 200 :headers { "Content-Type" "application/json"}}})
(defmethod routefn :default [resp] (apply resp []))


(defn handleRequest [req routes]
  (let [r (assemble-handlers routes)]
    (if (contains? r (:path req))
      (routefn
        (:handler (get r (:path req))))
      {:params {:status 404} :body "Not Found"})))
 

(defn extract-path [url]
  (.-pathname (new js/URL url)))

(defn convert-request [req]
  {:path (extract-path (.-url req))})

(defn worker [& routes] (js/addEventListener "fetch" 
  #(.respondWith % (make-response (handleRequest (convert-request (.-request %)) routes)))))
