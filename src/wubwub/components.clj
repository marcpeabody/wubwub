(ns wubwub.components
  (:require [seesaw.core :as see] [seesaw.bind :as sb]))

(defn slider [model-val min max cb]
  (let [slide (see/slider :min min :max max :value @model-val)
        slide-text (see/label :text (str @model-val))]
    (sb/bind model-val (sb/property slide-text :text))
    (sb/bind slide model-val)
    (sb/bind model-val slide)
    (sb/subscribe model-val (cb model-val))
    (sb/subscribe model-val (fn [e] (println "triggered")))
    (see/horizontal-panel :items [slide slide-text])
  )
)

(defn toggle-buttons [text-one action-one text-two action-two]
  (def one-button (see/button :text text-one))
  (def two-button (see/button :text text-two :enabled? false))
  (see/listen one-button :action (fn [e]
    (see/config! one-button :enabled? false)
    (see/config! two-button :enabled? true)
    (action-one)
  ))
  (see/listen two-button :action (fn [e]
    (see/config! one-button :enabled? true)
    (see/config! two-button :enabled? false)
    (action-two)
  ))
  (see/horizontal-panel :items [one-button two-button])
)
