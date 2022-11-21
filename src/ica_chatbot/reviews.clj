(ns ica-chatbot.reviews
  (:require [clojure.data.json :as json])
  (:require [ica-chatbot.secrets :as secrets]))


(defn print-review [review]
  (let [author (get review "author_name" "not available")
        text (get review "text" "not available")
        rating (get review "rating" "not available")
        date (get review "relative_time_description" "not available")]
   (println "Review: ")
   (println "Author: " author)
   (println "Date: " date)
   (println "Rating: " rating "stars")
   (println text)))


(defn fetch-latest-reviews [place-id]
  (let [url (format "https://maps.googleapis.com/maps/api/place/details/json?placeid=%s&key=%s" place-id secrets/api-key)
        response (get (json/read-str (slurp url)) "result" )
        reviews (get response "reviews")]
        (print-review (first reviews))
        (print-review (second reviews))
        (print-review (nth reviews 3))))

;; Klamovka place id (for testing)
;(fetch-latest-reviews "ChIJ0yzCP6aVC0cRrBAF4iVfwa0")