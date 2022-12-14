(ns ica-chatbot.regex
  (:require [clojure.string :as str]))

(defn match-regex
  "Matches input against regular expression (regex). Returns boolean"
  [regex input]
  (re-matches (re-pattern regex) input))

(defn get-intent
  "matches user input against regular expressions corresponding to user intents. Returns first match as an intent keyword"
  [input]
  (cond
    (not (nil? (match-regex #".*(bicycle|cycle|ride a bike|biking|cycling|bring a bike|ride a bicycle|ride bike|ride bicycle|bring bike).*" input))) :biking
    (not (nil? (match-regex #"^(?!where).*(toilet|wc|bathroom|restroom|rest rooms|w c|water closet).*" input))) :wc
    (not (nil? (match-regex #".*(attractions|what to see|what can I see|things to see|sights|sightseeing|memorial|nature trail|nature|trees).*" input))) :attractions
    (not (nil? (match-regex #".*(can|possible)*.*(skiing|ski).*" input))) :skiing
    (not (nil? (match-regex #".*(can|possible)*.*(skating|skate).*" input))) :skating
    (not (nil? (match-regex #"(?!.*transport).*(sport|sports|play sports|do sports|exercise|train|workout|work out|yoga|fitness).*" input))) :sports
    (not (nil? (match-regex #"^(?!where).*(playground|jungle gym|kids to play|kids play|kids can play|children to play|children can play).*" input))) :playground
    (not (nil? (match-regex #".*(bring a dog|dogs|dog|bring dogs|puppy).*" input))) :dogs
    (not (nil? (match-regex #".*(way to|get to|transportation|transport|commute).*" input))) :transportation
    (not (nil? (match-regex #"^(?!where).*(parking|car parking|car).*" input))) :parking
    (not (nil? (match-regex #".*(restaurant|cafe|bistro|food|breakfast|lunch|dinner).*" input))) :restaurant
    (not (nil? (match-regex #".*(review|testimonial|feedback|people say).*" input))) :reviews
    (not (nil? (match-regex #".*(finish|bye|done|exit|quit)" input))) :exit
    :else :unknown))

(defn parse-trams
  "Parses tram lines and stations from a string containing transportation info.
  Returns a map where keys are station names and values are corresponding lines"
  [t]
  (let [trams-str (re-find (re-pattern "(?:(?<=, )[^0-9]*|^(?:(?!Metro|metro|METRO|bus|Bus|BUS).)*)trams*[.]* +[Nn]o.[0-9, ]+") t)
        trams-seq (re-seq (re-pattern "[^0-9]*trams*[.]* +[Nn]o.[0-9, ]+") trams-str)
        trams-map (zipmap
                   (map #(first
                           (str/split % #"\s*trams*[.]* +[Nn]o.\s*")) trams-seq)
                   (map #(str/split (second
                                      (str/split % #"\s*trams*[.]* +[Nn]o.\s*")) #",\s*")
                                       trams-seq))]
    trams-map))

(defn parse-metro
  "Parses metro line/s and stations from a string containing transportation info.
   Returns a string."
  [t]
  (re-find (re-pattern "(?:.* - )*[Mm]etro .*?(?=$|, [A-Za-z]{2,})") t))
