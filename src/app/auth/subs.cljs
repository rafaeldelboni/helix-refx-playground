(ns app.auth.subs
  (:require [refx.alpha :as refx]))

(refx/reg-sub
 :app.auth/current-user
 (fn [db]
   (:current-user db)))

(refx/reg-sub
 :app.auth/login-loading
 (fn [db]
   (:login-loading? db)))

(refx/reg-sub
 :app.auth/login-error
 (fn [db]
   (:login-error db)))
