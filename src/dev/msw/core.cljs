(ns dev.msw.core
  (:require ["msw" :as msw]
            [dev.msw.config :as config]
            [dev.msw.mount :as mount]
            [dev.session-storage :as ss]))

(def ^:private ss-key "mock-active?")

(defonce mock-state (atom nil))

(defn start-browser [handlers]
  (when-not (nil? @mock-state)
    (.resetHandlers ^js/Object @mock-state))
  (reset! mock-state handlers)
  (-> (.start @mock-state (clj->js {:onUnhandledRequest "bypass"}))
      (.then #(ss/set-item! ss-key true))
      (.catch #(js/console.log %))))

(defn stop-browser []
  (.stop @mock-state)
  (reset! mock-state nil)
  (ss/remove-item! ss-key))

(defn start!
  ([] (start! config/default))
  ([custom-config]
   (start-browser (apply msw/setupWorker (mount/mount custom-config)))))

(defn stop! []
  (stop-browser))

(defn init! []
  (if (ss/get-item ss-key)
    (start!)
    (js/Promise.resolve)))
