(ns app.auth.events
  (:require [refx.alpha :as refx]))

(refx/reg-event-fx
 :app.auth/login-done
 (fn
   [{db :db} [_ [_ response]]]
   {:db (-> db
            (assoc :login-loading? false)
            (assoc :current-user response))}))

(refx/reg-event-db
 :app.auth/login-error
 (fn
   [db _]
   (-> db
       (assoc :login-loading? false)
       (assoc :current-user nil))))

(refx/reg-fx
 :login
 (fn [{:keys [user on-success]} _]
   (js/setTimeout #(refx/dispatch (conj on-success user)) 500)))

(refx/reg-event-fx
 :app.auth/login
 (fn
   [{db :db} user]
    ;; we return a map of (side) effects
   {:login {:user user
            :on-success [:app.auth/login-done]
            :on-failure [:app.auth/login-error]}
    :db  (assoc db :login-loading? true)}))

(refx/reg-event-db
 :app.auth/logout
 (fn
   [db _]
   (-> db
       (assoc :current-user nil))))
