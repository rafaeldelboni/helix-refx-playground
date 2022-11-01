(ns dev.msw.handlers
  (:require [dev.msw.helpers :refer [resp-post]]))

(defn login-handler [req res ctx]
  (apply
   res
   [(.delay ctx 500)
    (.status ctx 201)
    (.json ctx (clj->js {:ok true
                         :username (-> req .-body .-username)}))]))

(def handlers
  [(resp-post "/login" login-handler)])
