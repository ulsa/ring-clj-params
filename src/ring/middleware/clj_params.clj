(ns ring.middleware.clj-params)

(defn- clj-request?
  [req]
  (if-let [^String type (:content-type req)]
    (not (empty? (re-find #"^application/(vnd.+)?clojure" type)))))

(defn wrap-clj-params
  [handler]
  (fn [req]
    (if-let [body (and (clj-request? req) (:body req))]
      (let [bstr (slurp body)
            clj-params (read-string bstr)
            req* (assoc req
                   :clj-params clj-params
                   :params (merge (:params req) clj-params))]
        (handler req*))
      (handler req))))

