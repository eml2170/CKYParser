#!/bin/bash

#Add jar to CLASSPATH
export CLASSPATH=$CLASSPATH:json-simple-1.1.1.jar

#Compile code
javac *.java

#Generate counts from original training data
python count_cfg_freq.py parse_train_vert.dat > cfg.counts

#Replace training data with _RARE_ using count file
java VocabularyCloser

#Generate counts for modified training data
python count_cfg_freq.py parse_closed.dat > cfg_closed.counts

#Run CKY
java CKYAlgorithm

#Evaluate prediction file
python eval_parser.py parse_dev.key prediction_file