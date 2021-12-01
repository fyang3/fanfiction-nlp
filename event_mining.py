import csv, json, sys
import spacy
nlp = spacy.load("en_core_web_sm")



def event_mining(json_file):
    '''INPUT: Json file from running the character coreference (fanfic format)
    OUTPUT: JSON file with similar format'''
    dependencies = set()
    cluster_idx_map = harry_merged_char_index_map
    char_cluster = harry_merged_char_index_map # from post-processing conll files
    #char_cluster_events = {clu:{"subj":[],"obj":[]} for clu in char_cluster}
    #global_word_idx = 0
    #accumulated_word_idx = 0 # accumulated word idx till the i-1 sentence before
    event_dict = {chap_dict['document'],'clusters':[{'name':'','mentions':[]} for _ in range(len(all_unique_names))]}d

for id,sent in enumerate(INPUT_TEXT):
  corpus = nlp(sent)
  sent_length = len(sent.split())
  for token in corpus:
    # verb/noun/adj are triggers
      if token.pos == spacy.symbols.VERB or token.pos == spacy.symbols.ADJ or token.pos == spacy.symbols.NOUN:
          for argument in token.children:
              global_word_idx = accumulated_word_idx + argument.i
              if argument.dep_ in {"nsubj","nsubjpass","csubj","csubjpass"}:
                  char_cluster_idx = token_idx_to_char_cluster_idx(id,global_word_idx,cluster_idx_map)   # needs change -> map idx in particular sentence into char_cluster 
                  if char_cluster_idx != -1:  
                        # subjects[token.lemma_.lower()][argument.text.lower()] += 1 # updating subject dict
                      event = (token, argument.text, argument.dep_,sent) # WITHOUT lemma to fit downstream
                      char_cluster_events[char_cluster_idx]["subj"].append(event)
                       
              elif argument.dep_ in {"dobj", "iobj", "pobj", "obj"}:
                  char_cluster_idx = token_idx_to_char_cluster_idx(id,global_word_idx,cluster_idx_map)   # needs change -> map idx in particular sentence into char_cluster 
                  if char_cluster_idx != -1:  
                        # subjects[token.lemma_.lower()][argument.text.lower()] += 1 # updating subject dict
                      event = (token, argument.text, argument.dep_,sent) # need: substitue for resolved
                      char_cluster_events[char_cluster_idx]["obj"].append(event)
  accumulated_word_idx += sent_length
                    
char_cluster_events


if __name__=="__main__":
    pass