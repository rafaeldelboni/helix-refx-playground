(ns app.auth.events
  (:require [ajax.core :as ajax]
            [refx.alpha :as refx]
            [refx.http]))

(refx/reg-event-fx
 :app.auth/login-done
 (fn
   [{db :db} [_ response]]
   (println response)
   {:db (-> db
            (assoc :login-loading? false)
            (assoc :current-user response))}))

(refx/reg-event-db
 :app.auth/login-error
 (fn
   [db resp]
   (println resp)
   (-> db
       (assoc :login-loading? false)
       (assoc :current-user nil))))

(refx/reg-event-fx
 :app.auth/login
 (fn
   [{db :db} [_ login]]
   {:http-xhrio {:method          :post
                 :uri             "/login"
                 :params          login
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [:app.auth/login-done]
                 :on-failure      [:app.auth/login-error]}
    :db  (assoc db :login-loading? true)}))

(refx/reg-event-db
 :app.auth/logout
 (fn
   [db _]
   (-> db
       (assoc :current-user nil))))
