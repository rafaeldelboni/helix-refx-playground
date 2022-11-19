(ns dev.core
  (:require [app.core :as app]
            [dev.msw.core :as mock]
            [promesa.core :as p]))

(def debug? ^boolean goog.DEBUG)

(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")))

(defn ^:export init []
  (p/do (mock/init!)
        (dev-setup)
        (app/init)))

(comment
  "Start mock service worker"
  (p/do (mock/start!)
        (.reload js/location))

  "Stop mock service worker"
  (p/do (mock/stop!)
        (.reload js/location)))
