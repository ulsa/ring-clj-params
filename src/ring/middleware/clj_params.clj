(ns ring.middleware.clj-params)

(defn- clj-request?
  [req]
  (if-let [^String type (:content-type req)]
    (not (empty? (re-find #"^application/(vnd.+)?clojure" type)))))

(defn wrap-clj-params
  "Augments :params according to a parsed Clojure data literal request body.
  If read-opts is supplied, will be passed to read-string (requires 1.7.0)."
  ([handler] (wrap-clj-params handler nil))
  ([handler read-opts]
   (fn [req]
     (if-let [body (and (clj-request? req) (:body req))]
       (let [bstr (slurp body)
             clj-params (binding [*read-eval* false]
                          (if read-opts
                            (read-string read-opts bstr)
                            (read-string bstr)))
             req* (assoc req
                    :clj-params clj-params
                    :params (merge (:params req) clj-params))]
         (handler req*))
       (handler req)))))

