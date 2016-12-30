(ns makemake.core
  (:require [clojure.string :as string])
  (:gen-class))

(defn get-c-files [path]
  (filter #(.endsWith (.getName %) ".c")
          (file-seq (clojure.java.io/file path))))

(defn get-lines [file]
  (line-seq (java.io.BufferedReader. (java.io.FileReader. file))))

(defn get-local-includes [file]
  (filter #(.startsWith % "#include \"") 
          (map string/trim (get-lines file))))

(defn get-filename [include]
  (nth (string/split 
    (nth (string/split include #"\"") 1)
  #"\.") 0))

(defn get-includes [file]
  (map get-filename (get-local-includes file)))

(defn get-targets [path]
  (loop [files (get-c-files path)
         targets ()]
    (if (= 0 (count files))
      targets
    (recur (rest files) 
           (cons (list (.getName (first files)) (get-includes (first files))) 
                 targets)))))

(defn object-file [filename]
  (string/replace filename ".c" ".o"))

(defn create-rule [target]
  (let [file (first target)
        deps (nth target 1)]
  (str (object-file file) " : " file " " (string/join " " (map #(str % ".o") deps)) "\n"
       "\t gcc -ansi -Wall -g -c " file)))

(defn default-target [executable targets]
  (let [objects (map #(object-file (first %)) targets)]
  (str  executable " : " 
       (string/join " " objects) "\n"
       "\t gcc -o " executable " " (string/join " " objects))))


(defn generate-makefile [path executable]
  (let [targets (get-targets path)]
    (str
      (default-target executable targets) "\n"
      (string/join "\n" (map create-rule targets)) "\n"
      "clean : \n\trm " executable " *.o")))

(defn create-makefile [args]
  (spit "makefile" (generate-makefile (first args) (nth args 1))))

(defn usage []
  (println "usage: lein run {path} {executable_name}"))

(defn -main
  "Create a makefile!"
  [& args]
  (if (= 2 (count args)) (create-makefile args) (usage)))
