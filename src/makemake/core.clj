(ns makemake.core
  (:require [clojure.string :as string])
  (:gen-class))

(defn get-c-files [path]
  (filter #(.endsWith (.getName %) ".c")
          (file-seq (clojure.java.io/file path))))

(defn get-lines [file]
  (-> file
      (java.io.FileReader.)
      (java.io.BufferedReader.)
      (line-seq)))

(defn get-local-includes [file]
  (->> (get-lines file)
    (map string/trim)
    (filter #(.startsWith % "#include \""))))

(defn get-filename [include]
  (nth (string/split 
    (nth (string/split include #"\"") 1)
  #"\.") 0))

(defn get-includes [file]
  (map get-filename (get-local-includes file)))

(defn get-targets [files]
  (into {} (map #(hash-map % (get-includes %)) files)))

(defn object-file [filename]
  (string/replace filename ".c" ".o"))

(defn create-rules [targets]
  (into {}
    (for [[file deps] targets]
      (hash-map file
      (str (object-file file) " : " file " " (string/join " " (map #(str % ".o") deps)) "\n"
           "\tgcc -ansi -Wall -g -c " file)))))

(defn default-target [executable source-files]
  (let [objects (map object-file source-files)]
    (str  executable " : " 
      (string/join " " objects) "\n"
                   "\tgcc -o " executable " " (string/join " " objects))))

(defn write-makefile [targets path executable]
  (let [source-files (map #(.getName %) (keys targets))
        rules        (vals targets)]
    (spit "makefile" 
      (str
        (default-target executable source-files) "\n"
        (string/join "\n" rules) "\n"
        "clean : \n\trm " executable " *.o"))))

(defn create-makefile [[path executable]]
  (-> (get-c-files path)
    (get-targets)
    (create-rules)
    (write-makefile path executable)))

(defn usage []
  (println "usage: lein run {path} {executable_name}"))

(defn -main
  "Create a makefile!"
  [& args]
  (if (= 2 (count args)) (create-makefile args) (usage)))
