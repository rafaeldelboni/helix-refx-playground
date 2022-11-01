(ns dev.core
  (:require [app.core :as app]
            [dev.msw.browser :as browser]
            [promesa.core :as p]))

(def debug? ^boolean goog.DEBUG)

(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")))

(defn ^:export init []
  (p/do (browser/init!)
        (dev-setup)
        (app/init)))

(comment
  "Start mock service worker"
  (p/do (browser/start!)
        (.reload js/location))

  "Stop mock service worker"
  (p/do (browser/stop!)
        (.reload js/location)))
