(ns hello-seesaw.core
  (:require [seesaw.core :as see])
  (:use overtone.live))

  (definst foo [] (saw 220))
  (def f (see/frame :title "Get to know Seesaw" :on-close :exit))

  (defn display [content]
    (see/config! f :content content)
    content)

  (def b (see/button :text "Click here to make it stop!!!"))

  (def x (foo))

  (defn -main [& args]
    (see/invoke-later
      (-> f see/pack! see/show!)
      (display b)
      (see/listen b :action (fn [e] (kill x) (see/alert e "Thanks!")))
      ))
