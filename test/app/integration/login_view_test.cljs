(ns app.integration.login-view-test
  (:require [app.auth.events]
            [app.auth.subs]
            [app.auth.views :as auth.views]
            [app.aux :as aux]
            [cljs.test :refer [async deftest is use-fixtures]]))

(use-fixtures :each
  {:after (fn [] (aux/cleanup))})

(deftest login-view-test
  (async done
         (let [container (-> auth.views/login-view aux/render .-container)
               button (-> container (aux/tag "button") first)
               _click (aux/click button)]
           (js/setTimeout
            (fn []
              (is (= "Error... Try again"
                     (-> container
                         (aux/query "#login-error")
                         aux/text)))
              (done))
            500))))
