(ns app.http
  (:require [lambdaisland.fetch :as fetch]
            [refx.alpha :refer [dispatch reg-fx]]))

(defn- js->cljs-key [obj]
  (js->clj obj :keywordize-keys true))

(defn- send-request!
  [{:keys [url on-success on-failure] :as request}]
  (-> (fetch/request url request)
      (.then (fn [{:keys [status] :as resp}]
               (if (> status 400)
                 (dispatch (conj on-failure (js->cljs-key resp)))
                 (dispatch (conj on-success (js->cljs-key resp))))))
      (.catch (fn [resp]
                (dispatch (conj on-failure (js->cljs-key resp)))))))

(defn http-effect
  [request]
  (if (sequential? request)
    (doseq [r request]
      (send-request! r))
    (send-request! request)))

(reg-fx :http http-effect)
