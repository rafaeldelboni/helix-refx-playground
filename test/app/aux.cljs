(ns app.aux
  (:require ["@testing-library/react" :as tlr]
            [helix.core :refer [$]]))

(defn text [el]
  (.-textContent el))

(defn length [el]
  (.-length el))

(defn get-class
  [^js/Element el]
  (.toString (.-classList el)))

(defn click
  [^js/Element el]
  (.click tlr/fireEvent el))

(defn tag
  [element tag-name]
  (.getElementsByTagName element tag-name))

(defn query
  [element query]
  (.querySelector element query))

(defn testing-container []
  (as-> (js/document.createElement "div") div
    (js/document.body.appendChild div)))

(defn render
  [component]
  (tlr/render ($ component)
              #js {:container (testing-container)}))

(defn cleanup
  ([] (tlr/cleanup))
  ([after-fn] (tlr/cleanup)
              (after-fn)))
