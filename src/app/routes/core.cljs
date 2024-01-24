(ns app.routes.core 
  (:require [refx.alpha :as refx]
            [reitit.coercion.schema :as rsc]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]))

(defn router [routes]
  (rf/router
   routes
   {:data {:controllers [{:start #(js/console.log "start" "root controller")
                          :stop #(js/console.log "stop" "root controller")}]
           :coercion rsc/coercion
           :public? false}}))

(defn on-navigate [new-match]
  (when new-match
    (refx/dispatch [:app.routes/navigated new-match])))

(defn init-routes! [routes]
  (js/console.log "initializing routes")
  (rfe/start!
   (router routes)
   on-navigate
   {:use-fragment true}))
