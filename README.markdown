# ring-clj-params

A [Ring](https://github.com/mmcgrana/ring) middleware that augments :params according to a parsed [Clojure](http://clojure.org) data literal request body.

## Usage

### Leiningen

In your `:dependecies` section add the following:

```clojure
    [ring-clj-params "0.1.0"]
```

### Ring

To use this middleware using Ring and [Compojure]():

```clojure
(ns my-awesome-service
  (:use compojure.core)
  (:use ring.middleware.clj-params))

(defn generate-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/clojure"}
   :body (print-str data)})
  
(defroutes handler
  (GET "/" []
       (generate-response {:hello :cleveland}))

  (PUT "/" [name]
       (generate-response {:hello name})))

(def app
  (-> handler
      wrap-clj-params))
```

Run this app as you normally would and try the following at the command line:

```sh
curl -X GET http://localhost:8080/

#=> {:hello :cleveland}                                                                                                                         curl -X PUT -H "Content-Type: application/clojure" \ 
  -d '{:name :barnabas}' \
  http://localhost:8080/ 

#=> {:hello :barnabas}%  
```

## Acknowledgment(s)

Thanks to [Mark McGranaghan](http://markmcgranaghan.com/) for his work on Ring and [ring-json-params](https://github.com/mmcgrana/ring-json-params) on which this project was based.

## License

Copyright (C) 2012 Fogus

Distributed under the Eclipse Public License, the same as Clojure.
