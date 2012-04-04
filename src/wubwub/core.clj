(ns wubwub.core
  (:require [seesaw.core :as see] [seesaw.bind :as sb])
  (:use overtone.live wubwub.wubs wubwub.components))

(def f (see/frame :title "Wub Wub" :on-close :exit))
(defn display [content]
  (see/config! f :content content)
  content)

(def wobble-val (atom 1))
(def note-val (atom 60))
(defn start-dubstep [] (def wub (dubstep :wobble @wobble-val :note @note-val)))

(def wobble-slider (slider wobble-val 1 10
  (fn [model-val] (fn [e] (ctl wub :wobble @model-val))
)))
(def note-slider (slider note-val 48 72
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
      (sb/notify note-val 72)

      ;;wait one second
      (Thread/sleep 1000)

      ;;set the note back to what it was
      (sb/notify note-val temp-val)

      (see/config! woo-button :enabled? true)
    )))
  )
))

(def kb (midi-in "Oxygen"))

(def midi-log* (ref []))
(def controls* (ref {}))
(def notes* (ref {}))

(defn midi-listener [event ts]
  (println "listener: " event)
  (try
    (let [note (:note event)
          id   (get @notes* note)]
       (println "NOTE: " note)
       (sb/notify note-val note))
    (catch java.lang.Exception e (println "midi-listener exception: \n" e))))

(defn control-watcher [ctl-num, _, old-val, new-val]
  (println ctl-num ":" new-val))

(.start (Thread. (fn []
;;threading issue - not always triggering the listener
  (midi-handle-events kb #'midi-listener)
)))


(defn -main [& args]
  (see/invoke-later
    (start-dubstep)
    (display (see/border-panel
               :west (see/horizontal-panel :items [wobble-slider note-slider woo-button])
               :south buttons-panel))
    (-> f see/pack! see/show!)
  )
)
