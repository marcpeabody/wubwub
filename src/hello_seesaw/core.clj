(ns hello-seesaw.core
  (:require [seesaw.core :as see] [seesaw.bind :as sb])
  (:use overtone.live))

  (def f (see/frame :title "Get to know Seesaw" :on-close :exit))

  (defn display [content]
    (see/config! f :content content)
    content)

  (def b (see/button :text "Click here to make it stop!!!"))
  (def bup (see/button :text "Set frequency to the slider position"))
  (def slide (see/slider :min 220 :max 440))
  (def slide-val (atom 220))
  (sb/bind slide slide-val)

  (definst foo [freq 220] (saw freq))
  (def x (foo))

  (defn -main [& args]
    (see/invoke-later
      (display (see/border-panel
                 :north slide
                 :center b
                 :south bup))
      (-> f see/pack! see/show!)
      (see/listen b :action (fn [e] (kill x) (see/alert e "Thanks!")))
      (see/listen bup :action (fn [e] (ctl foo :freq @slide-val)))
      ))
