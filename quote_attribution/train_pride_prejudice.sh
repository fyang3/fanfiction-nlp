#!/bin/bash
BOOKNLP_BASE="/usr0/home/mamille2/book-nlp"

python run.py prepare-train --story-path example_train_data/pride_prejudice.csv --char-path example_train_data/pride_prejudice.chars --ans-path example_train_data/pride_prejudice.ans --tok-path example_train_data/pride_prejudice.tok --output-path example_train_data/pride_prejudice.svmrank --features disttoutter spkappcnt nameinuttr spkcntpar --booknlp ${BOOKNLP_BASE} --threads 1 --no-cipher-char --no-coref-story
