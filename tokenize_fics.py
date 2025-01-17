import spacy
from tqdm import tqdm
import os, sys
#import pandas as pd
import csv
from multiprocessing import Pool
import pdb

nlp = spacy.load('en_core_web_sm')
#data_dirpath = '/data/fanfiction_ao3/{0}/complete_en_1k-50k/fics'
data_dirpath = 'sense_and_sensibility_test'
input_format = 'txt'
csv.field_size_limit(sys.maxsize)


def tokenize(text):
    return ' '.join([tok.text for tok in nlp.tokenizer(text)])


def tokenize_fics(fandom):
    fandom_dirpath = data_dirpath.format(fandom)

    for fname in tqdm(os.listdir(fandom_dirpath)):
        fpath = os.path.join(fandom_dirpath, fname)

        if input_format == 'csv': #currently just ignore
            pass
            # pandas pretty much as fast
            #data = pd.read_csv(fpath)
            #data['text_tokenized'] = data['text'].map(tokenize)
            #data.to_csv(fpath, index=False)

            with open(fpath) as f:
                reader = csv.reader(f)

                # Deal with the header
                hdr = next(reader)
                if len(hdr) == 4:
                    outlines = [hdr + ['text_tokenized']]
                elif len(hdr) == 5: # already done
                    continue
                    #outlines = [hdr]

                for row in reader:
                    if len(row) < 3:
                        continue
                    text = row[3]
                    outlines.append(row[:4] + [tokenize(text)])

            with open(fpath, 'w') as f:
                writer = csv.writer(f)
                writer.writerows(outlines)

        else: # for txt files
            with open(fpath) as file_obj:
                sentences = file_obj.read().splitlines()
                headers = ['fic_id','chapter_id','para_id','text','text_tokenized']
                data = [headers]
                para_id = 0
                for i in range(len(sentences)):
                    if len(sentences[i])>2:
                        entry = [1,29,para_id,sentences[i],tokenize(sentences[i])]
                        #sent_toks.append([tok.text for tok in nlp.tokenizer(sent.lower())])
                        data.append(entry)
                        para_id += 1

            with open(fpath[:-4] + '.csv', 'w') as wfile:
                #wfile.write(headers)
                #for sent_tok in sent_toks:
                csvwriter = csv.writer(wfile)
                csvwriter.writerows(data)
                    #wfile.write(' '.join(sent_tok) + '\n') # change to write 5 entries


def main():
    # Settings
    fandoms = [
    #    'allmarvel',
    #    'supernatural',
    #    'harrypotter',
    #    'dcu',
    #    'sherlock',
    #    'teenwolf',
    #    'starwars',
    #    'drwho',
    #    'tolkien',
    #    'dragonage',
    # 'example_fandom'
    'sense_and_sensibility_test'
    ]

    #data_fpath = '/usr0/home/mamille2/erebor/fanfiction-project/data/ao3/{0}/ao3_{0}_sentences.txt'

    #with Pool(10) as p:
    #    list(tqdm(p.imap(tokenize_fics, fandoms), total=10))

    # without multiprocessing for debugging
    list(map(tokenize_fics, fandoms))

    #for i,fandom in enumerate(fandoms):
        #print(f'{i}/{len(fandoms)} {fandom}')

if __name__ == '__main__':
    main()
    #file_input_dir = sys.argv[1]
    #tokenize_fics(file_input_dir)

