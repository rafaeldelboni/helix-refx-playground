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
