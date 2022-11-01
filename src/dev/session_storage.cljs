(ns dev.session-storage)

(defn set-item!
  [key val]
  (.setItem (.. js/window -sessionStorage) key val))

(defn get-item
  [key]
  (.getItem (.-sessionStorage js/window) key))

(defn remove-item!
  [key]
  (.removeItem (.-sessionStorage js/window) key))
