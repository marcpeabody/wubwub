(ns wubwub.core
  (:require [seesaw.core :as see] [seesaw.bind :as sb])
  (:use overtone.live wubwub.wubs wubwub.components))

(def f (see/frame :title "Wub Wub" :on-close :exit))
(defn display [content]
  (see/config! f :content content)
  content)

(def wub (dubstep))

(def wobble-val (atom 1))

(def slider-panel (slider wobble-val 1 10
  (fn [model-val] (fn [e] (ctl wub :wobble @model-val))
)))

(def buttons-panel (toggle-buttons "Stop" stop
                                   "Start" (fn [] (def wub (dubstep :wobble @wobble-val)))))

(defn -main [& args]
  (see/invoke-later
    (display (see/border-panel
               :west slider-panel
               :south buttons-panel))
    (-> f see/pack! see/show!)
  )
)
