(ns dev.msw.helpers
  (:require ["msw" :rename {rest Rest}]))

(defn resp-get
  [path handler]
  (.get Rest path handler))

(defn resp-post
  [path handler]
  (.post Rest path handler))
