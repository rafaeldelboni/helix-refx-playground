(ns dev.msw.mount
  (:require ["msw" :as msw]))

(defn resolve-body-fn [{:keys [body]} args] (apply body args))

(defn resolve-body-map [{:keys [lag content-type body status]} [_req res ctx]]
  (res (when lag (.delay ctx lag))
       (when status (.status ctx (or status :200)))
       (when body (.call (aget ctx (name content-type)) ctx body))))

(defn resolve-body [options args]
  (if (fn? options)
    (resolve-body-fn options args)
    (resolve-body-map options args)))

(defn mount
  [mock-map]
  (map (fn [[url url-config]]
         (let [[method options] (apply vec url-config)
               rest-method (aget msw/rest (name method))]
           (.call rest-method msw/rest url
                  (fn [& args]
                    (resolve-body options args)))))
       mock-map))
