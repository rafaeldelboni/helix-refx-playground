(ns app.integration.auth-view-test
  (:require [app.auth.events]
            [app.auth.subs]
            [app.auth.views :as auth.views]
            [app.aux :as aux]
            [cljs.test :refer [async deftest is testing use-fixtures]]
            [dev.msw.core :as mock]
            [promesa.core :as p]
            [refx.alpha :as refx]))

(refx/reg-event-db
 :test/initialize-db
 (fn [_ _]
   {:current-route nil
    :current-user nil
    :login-loading? false
    :login-error nil}))

(use-fixtures :each
  {:before #(async done
                   (refx/clear-subscription-cache!)
                   (refx/dispatch-sync [:test/initialize-db])
                   (aux/cleanup)
                   (done))})

(def login-error-msw
  {"/login"
   {:post {:lag 300
           :status 500
           :content-type :json
           :body #js {:ok false}}}})

(deftest auth-view-email-send-error-test
  (testing "auth email send view should error"
    (async done
           (p/let [_ (mock/start! login-error-msw)
                   render (-> auth.views/login-view aux/render)
                   container (.-container render)
                   button (-> container (aux/tag "button") first)
                   input (-> container (aux/query "#login-username"))
                   _change (aux/wait-for #(-> input (aux/change "err@ee.cc")))
                   _click (aux/wait-for #(-> button aux/click))
                   error-msg (aux/wait-for #(-> container (aux/query "#login-error") aux/text))]
             (is (= "Error... try it again." error-msg))
             (aux/cleanup)
             (done)))))
