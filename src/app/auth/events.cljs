(ns app.auth.events
  (:require [app.http]
            [refx.alpha :as refx]))

(refx/reg-event-fx
 :app.auth/login-done
 (fn
   [{db :db} [_ response]]
   (println :success response)
   {:db (-> db
            (assoc :login-loading? false)
            (assoc :login-error nil)
            (assoc :current-user (:body response)))}))

(refx/reg-event-db
 :app.auth/login-error
 (fn
   [db response]
   (println :fail response)
   (-> db
       (assoc :login-loading? false)
       (assoc :login-error response)
       (assoc :current-user nil))))

(refx/reg-event-fx
 :app.auth/login
 (fn
   [{db :db} [_ login]]
   {:http {:method      :post
           :url         "/login"
           :body        login
           :accept :json
           :content-type :json
           :on-success  [:app.auth/login-done]
           :on-failure  [:app.auth/login-error]}
    :db  (assoc db
                :login-error nil
                :login-loading? true)}))

(refx/reg-event-db
 :app.auth/logout
 (fn
   [db _]
   (-> db
       (assoc :current-user nil))))
