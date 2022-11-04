(ns app.integration.login-view-test
  (:require [app.auth.events]
            [app.auth.subs]
            [app.auth.views :as auth.views]
            [app.aux :as aux]
            [cljs.test :refer [async deftest is]]
            [dev.msw.browser :as msw]
            [promesa.core :as p]))

(deftest login-view-test
  (async done
         (p/do
           (aux/wait-for
            #(p/let [_ (msw/start!)
                     cc (-> auth.views/login-view aux/render)
                     container (-> cc .-container)
                     button (-> container (aux/tag "button") first)
                     input (-> container (aux/query "#login-username"))
                     _change (-> input (aux/change "err@ee.cc"))
                     _click (-> button aux/click)
                     error-msg (-> cc (aux/find-by-text "Error... Try again"))]
               (is (= "Error... Try again" (aux/text error-msg)))
               (aux/cleanup done))))))
