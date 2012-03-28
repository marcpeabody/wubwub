(ns wubwub.core
  (:require [seesaw.core :as see] [seesaw.bind :as sb])
  (:use overtone.live wubwub.wubs wubwub.components))

(def f (see/frame :title "Wub Wub" :on-close :exit))
(defn display [content]
  (see/config! f :content content)
  content)

(def wobble-val (atom 1))
(def note-val (atom 50))
(defn start-dubstep [] (def wub (dubstep :wobble @wobble-val :note @note-val)))
(start-dubstep)

(def wobble-slider (slider wobble-val 1 10
  (fn [model-val] (fn [e] (ctl wub :wobble @model-val))
)))
(def note-slider (slider note-val 30 70
  (fn [model-val] (fn [e] (ctl wub :note @model-val))
)))

(def buttons-panel (toggle-buttons "Stop" stop
                                   "Start" start-dubstep))

(def woo-button (see/button :text "Woo"))
(see/listen woo-button :action (fn [e]
  (let [temp-val @note-val]
    (.start (Thread. (fn []
      (see/config! woo-button :enabled? false)

      ;;woo maxes out the note pitch
      (sb/notify note-val 70)

      ;;wait one second
      (Thread/sleep 1000)

      ;;set the note back to what it was
      (sb/notify note-val temp-val)

      (see/config! woo-button :enabled? true)
    )))
  )
))

(defn -main [& args]
  (see/invoke-later
    (display (see/border-panel
               :west (see/horizontal-panel :items [wobble-slider note-slider woo-button])
               :south buttons-panel))
    (-> f see/pack! see/show!)
  )
)
