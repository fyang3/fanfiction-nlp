import csv, json, sys

def internal_merge(chap_json):
    with open(chap_json,'r') as f:
        chap_dict = json.load(f)
        char_dict = chap_dict['clusters']
        num_chars = len(char_dict)
        all_unique_names = list(set([char['name'] for char in char_dict]))
        merged_dict = {'document':chap_dict['document'],'clusters':[{'name':'','mentions':[]} for _ in range(len(all_unique_names))]}
        for clu in char_dict:
            name = clu['name']
            idx = all_unique_names.index(name)
            merged_dict['clusters'][idx]['name'] = name
            merged_dict['clusters'][idx]['mentions'] += clu['mentions']

        #print([clu['name'] for clu in merged_dict['clusters']]) #sanity check
        with open(chap_json[:-5]+' merged.json','w') as f2:
            json.dump(merged_dict, f2)
            return merged_dict

def merge_two_chapters_clusters(chapt1_json,chapt2_json):
    '''INPUT: Chap1,Chap2 json
       OUTPUT: merged json with same headers
       note:this only merge characters that overlap in the 2 chapters - filters lots of smaller chars that only
       appear in 1 chapter, but might be problematic later'''
    with open(chapt1_json,'r') as f:
        chapt1_dict = json.load(f)
        chapt1_dict = internal_merge(chapt1_json)
        with open(chapt2_json,'r') as f1:
            chapt2_dict = json.load(f1)
            chapt2_dict = internal_merge(chapt2_json)
            last_idx_chap1 = len(chapt1_dict['document'])
            new_dict = {"document":chapt1_dict['document'] + chapt2_dict['document'],'clusters':[]}
            # "clusters":[{'name':'','mentions':[]} for _ in range(int(idx_map_list[-1][0]))]}
            chapt1_clusters = chapt1_dict['clusters']
            chapt2_clusters = chapt2_dict['clusters']
            for clu1 in chapt1_clusters:
                for clu2 in chapt2_clusters:
                    shifted_mentions = [{'position':[ment['position'][0]+last_idx_chap1,ment['position'][1]+last_idx_chap1],'text':ment['text']} for ment in clu2['mentions']]
                    if clu1['name'] == clu2['name']:
                        #merge
                        #shifted_mentions = [{'position':[ment['position'][0]+last_idx_chap1,ment['position'][1]+last_idx_chap1]} for ment in clu2['mentions']]
                        merged_clu = {'name':clu1['name'],'mentions':[clu1['mentions']+shifted_mentions]}
                        new_dict['clusters'].append(merged_clu)
            
            print([clu['name'] for clu in new_dict['clusters']]) 

            with open('merged_chapters.json','w') as f:
                json.dump(new_dict,f)
                return new_dict

            


def merge_all_chapters(dir,outfile_name):
    '''INPUT: a dir of the json outputs for all chapters from a book
       OUTPUT: a large json file with everything, export json'''
    pass


def get_char_stats(char_json):
    '''INPUT: json file of char
       OUTPUT: readable stats report (maybe csv?) on char status '''
    pass



if __name__=="__main__":
    chap1 = sys.argv[1]
    chap2 = sys.argv[2]
    #internal_merge(chap1)
    merge_two_chapters_clusters(chap1,chap2)
