(ns app.auth.views
  (:require [app.lib :refer [defnc]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            [refx.alpha :as refx]))

(defnc login-view []
  (let [loading? (refx/use-sub [:app.auth/login-loading])
        error? (refx/use-sub [:app.auth/login-error])
        [state set-state] (hooks/use-state {:username "" :password ""})]
    (d/div
     (d/form
      {:disabled loading?
       :on-submit (fn [e]
                    (.preventDefault e)
                    (when (and (:username state)
                               (:password state))
                      (refx/dispatch [:app.auth/login state])))}

      (d/label "Username")
      (d/input
       {:id "login-username"
        :value (:username state)
        :disabled loading?
        :on-change #(set-state assoc :username (.. % -target -value))})

      (d/label "Password")
      (d/input
       {:value (:password state)
        :disabled loading?
        :on-change #(set-state assoc :password (.. % -target -value))})

      (d/button
       {:type "submit"}
       "Login")

      (when loading?
        (d/p
         {:id "login-loading"}
         "Loading..."))

      (when error?
        (d/p
         {:id "login-error"}
         "Error... Try again"))))))
