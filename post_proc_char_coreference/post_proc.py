'''
preprocess a json file output for char coreference
    document: string of tokenized text (space between each token)
    clusters: a list of character clusters, each with the fields:
        name
        mentions: a list of mentions, each a dictionary with keys:
            position: [start_token_id, end_token_id+1]. The position of the start token of the mention in the document list (inclusive), and the position 1 after the last token in the mention in the document list.
            text: The text of the mention

'''
import json, os, csv
import sys


dir_name = '../fanfiction-nlp/' #optional for future usage for all files in a dir

# read the file and extract char clusters

def get_char_names_stats(filename):
    with open(filename,'r',encoding='utf-8') as f:
        char_dict = json.load(f)['clusters']
        num_chars = len(char_dict)
        char_names = [char['name'] for char in char_dict]
        char_mentions = []
        for i in range(num_chars):
            mentions = char_dict[i]['mentions'] # a list of dict
            print('mentions: ',mentions)
            print(type(mentions))
            mention_names = []
            for k in range(len(mentions)):
                for j in range(len(mentions[k])):
                    mention_names.append(mentions[k][j]['text'])
            #mention_names = [ment['text'] for ment in mentions]
            name = char_dict[i]['name']
            char_mentions.append([name,mention_names])
        # internal merge
        
        print('Total character detected: {} \n'.format(num_chars))
        print('Character names: {} \n'.format(char_names))
        print('Character names with mentions: {} \n'.format(char_mentions))
        return char_mentions


def main():
    pass

if __name__ == '__main__':
    filename = sys.argv[1]
    chars = get_char_names_stats(filename)
