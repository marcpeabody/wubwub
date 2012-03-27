(ns wubwub.core
  (:require [seesaw.core :as see] [seesaw.bind :as sb])
  (:use overtone.live wubwub.wubs))

(definst foo [freq 220] (saw freq))

(def f (see/frame :title "Wub Wub" :on-close :exit))

(defn display [content]
  (see/config! f :content content)
  content)

(def wobble-val (atom 1))
(def wub (dubstep))

(def stop-button (see/button :text "Stop"))
(def start-button (see/button :text "Start" :enabled? false))

(defn click-stop [e]
  (see/config! stop-button  :enabled? false)
  (see/config! start-button :enabled? true)
  (stop)
)
(see/listen stop-button :action click-stop)

(defn click-start [e]
  (see/config! start-button :enabled? false)
  (see/config! stop-button  :enabled? true)
  (def wub (dubstep :wobble @wobble-val))
)
(see/listen start-button :action click-start)

(def wobble-slide (see/slider :min 1 :max 10 :value @wobble-val))
(def wobble-slide-text (see/label :text (str @wobble-val)))

(sb/bind wobble-val (sb/property wobble-slide-text :text))
(sb/bind wobble-slide wobble-val)
(sb/subscribe wobble-val (fn [e] (ctl wub :wobble @wobble-val)))


(defn -main [& args]
  (see/invoke-later
    (display (see/border-panel
               :west (see/horizontal-panel :items [wobble-slide wobble-slide-text])
               :south (see/horizontal-panel :items [stop-button start-button])))
    (-> f see/pack! see/show!)
  )
)
