(ns dev.msw.config)

(def default
  {"/login"
   {:post {:status 201
           :content-type :json
           :body #js {:ok true
                      :username "msk.mock"}}}})
