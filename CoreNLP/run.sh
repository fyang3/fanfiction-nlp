java -Xmx5g -cp stanford-corenlp-3.9.2.jar:stanford-corenlp-3.9.2-models.jar edu.stanford.nlp.coref.CorefSystem $1 $2 $3 -annotators tokenize,ssplit,pos,lemma,ner,parse,mention,coref -coref.algorithm clustering > coref.temp

