(ns hello-seesaw.core
  (:require [seesaw.core :as see] [seesaw.bind :as sb])
  (:use overtone.live))

(definst foo [freq 220] (saw freq))

(def f (see/frame :title "Ooooo Waaaaah" :on-close :exit))

(defn display [content]
  (see/config! f :content content)
  content)

(def slide-val (atom 440))
(def x (foo :freq @slide-val))

(def stop-button (see/button :text "Stop"))
(def start-button (see/button :text "Start" :enabled? false))

(defn click-stop [e]
  (see/config! stop-button  :enabled? false)
  (see/config! start-button :enabled? true)
  (kill x)
)
(see/listen stop-button :action click-stop)

(defn click-start [e]
  (see/config! start-button :enabled? false)
  (see/config! stop-button  :enabled? true)
  (def x (foo :freq @slide-val))
)
(see/listen start-button :action click-start)

(def slide (see/slider :min 220 :max 440 :value @slide-val))
(def slide-text (see/label :text (str @slide-val "Hz")))

(sb/bind slide-val (sb/property slide-text :text))
(sb/bind slide-val (sb/transform #(str % "Hz")) (sb/property slide-text :text))
(sb/bind slide slide-val)
(sb/subscribe slide-val (fn [e] (ctl foo :freq @slide-val)))

(defn -main [& args]
  (see/invoke-later
    (display (see/border-panel
               :west (see/horizontal-panel :items [slide slide-text])
               :south (see/horizontal-panel :items [stop-button start-button])))
    (-> f see/pack! see/show!)
  )
)

