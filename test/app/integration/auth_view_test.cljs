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

(defn login-input-send [input-text container]
  (p/do
    (aux/wait-for #(-> container
                       (aux/query "#login-username")
                       (aux/change input-text)))
    (aux/wait-for #(-> container
                       (aux/tag "button")
                       first aux/click))))

(defn get-login-error-msg [container]
  (aux/wait-for #(-> container
                     (aux/query "#login-error")
                     aux/text)))

(deftest auth-view-email-send-error-test
  (testing "auth email send view should error"
    (async done
      (p/let [_ (mock/start! login-error-msw)
              container (-> auth.views/login-view aux/render .-container)
              _input (login-input-send "email@email.com" container)
              error-msg (get-login-error-msg container)]
        (is (= "Error... try it again." error-msg))
        (aux/cleanup)
        (done)))))
