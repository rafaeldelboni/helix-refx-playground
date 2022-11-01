(ns app.core
  (:require ["react-dom/client" :as rdom]
            [app.auth.events]
            [app.auth.subs]
            [app.auth.views :as auth.views]
            [app.lib :refer [defnc]]
            [app.routes.core :as routes]
            [app.routes.events]
            [app.routes.subs]
            [clojure.pprint :refer [pprint]]
            [helix.core :refer [$]]
            [helix.dom :as d]
            [refx.alpha :as refx]
            [refx.db :as db]
            [reitit.frontend.easy :as rfe]
            [schema.core :as s]))

;;; Events ;;;

(refx/reg-event-db ::initialize-db
                   (fn [db _]
                     (if db
                       db
                       {:current-route nil
                        :current-user nil})))

;;; Views ;;;

(defnc home-page []
  (d/div
   (d/h2 "Welcome to frontend")
   (d/p "Look at console log for controller calls.")))

(defnc item-page [match]
  (let [{:keys [path query]} (:parameters match)
        {:keys [id]} path]
    (d/div
     (d/ul
      (d/li (d/a {:href (rfe/href ::item {:id 1})} "Item 1"))
      (d/li (d/a {:href (rfe/href ::item {:id 2} {:foo "bar"})} "Item 2")))
     (when id
       (d/h2 "Selected item " id))
     (when (:foo query)
       (d/p "Optional foo query param: " (:foo query))))))

(defnc about-page []
  (d/div
   (d/p "This view is public.")))

(defnc main-view []
  (let [{:keys [username] :as user} (refx/use-sub [:app.auth/current-user])
        match (refx/use-sub [:app.routes/current-route])
        route-data (:data match)]
    (d/div
     (d/ul
      (d/li (d/a {:href (rfe/href ::frontpage)} "Frontpage"))
      (d/li (d/a {:href (rfe/href ::about)} "About (public)"))
      (d/li (d/a {:href (rfe/href ::item-list)} "Item list"))
      (when user
        (d/div
         (d/li (d/a {:on-click (fn [e]
                                 (.preventDefault e)
                                 (refx/dispatch [:app.auth/logout]))
                     :href "#"}
                    (str "Logout (" username ")"))))))
     ;; If user is authenticated
     ;; or if this route has been defined as public, else login view
     (if (or user (:public? route-data))
       (when-let [view (:view route-data)]
         ($ view {:match match}))
       ($ auth.views/login-view))
     (d/pre (with-out-str (pprint @db/app-db))))))

;;; Routes ;;;

(def routes
  ["/"
   [""
    {:name ::frontpage
     :view home-page}]

   ["about"
    {:name ::about
     :view about-page
     :public? true}]

   ["login"
    {:name ::login
     :view auth.views/login-view
     :public? true}]

   ["items"
    {:view item-page}
    [""
     {:name ::item-list}]
    ["/:id"
     {:name ::item
      :parameters {:path {:id s/Int}
                   :query {(s/optional-key :foo) s/Keyword}}}]]])

;;; Startup ;;;

(defonce root
  (rdom/createRoot (js/document.getElementById "app")))

(defn render []
  (.render root ($ main-view)))

(defn ^:export init []
  (refx/clear-subscription-cache!)
  (refx/dispatch-sync [::initialize-db])
  (routes/init-routes! routes)
  (render))
