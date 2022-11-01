(ns dev.msw.browser
  (:require ["msw" :as msw]
            [dev.msw.handlers :as handlers]
            [dev.session-storage :as ss]))

(def ^:private mock-key "mock-active?")

(def ^:private ^js/object worker (apply msw/setupWorker handlers/handlers))

(defn- start-worker! []
  (.start worker (clj->js {:onunhandledrequest "bypass"})))

(defn start! []
  (->  (start-worker!)
       (.then #(ss/set-item! mock-key true))))

(defn stop! []
  (.stop worker)
  (ss/remove-item! mock-key))

(defn init! []
  (if (ss/get-item mock-key)
    (start-worker!)
    (js/Promise.resolve)))
