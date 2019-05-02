#!/usr/bin/env python3 -u
# -*- coding: utf-8 -*-
"""
Do quote attribution on one or across multiple CPUs.
"""

import os
import argparse
import feature_extracters
from predict import predict
from train import train
from feature_extracters import add_extracter_args, EXTRACTER_REGISTRY


def main():
    """
    The main function of quote attribution. Run it in either the 'predict' mode 
    or the 'train' mode. 
    
    In the 'predict' mode, the program will take directories or single files 
    (output by the coreference resolution pipeline) as input, and produce quote 
    attribution json files to the designated path.

    In the 'train' mode, the program will take preprocessed quote annotation
    datasets (also output by coreference resolution pipeline) to train a new
    svm-rank model.
    """

    # Parse arguments
    parser = argparse.ArgumentParser()
    parser.add_argument('mode', choices=('predict', 'train'),
                        help='running mode')
    parser.add_argument('--story-path', required=True,
                        help='Path to the coreference resolved story csv file or '
                             'the directory that contains the story csv files to '
                             'be processed')
    parser.add_argument('--char-path', required=True, 
                        help='Path to the coreference resolved character list '
                             'file or the directory that contains the character '
                             'list files')
    parser.add_argument('--output-path',
                        help='Path to the output file or the the directory to'
                             'output results')
    parser.add_argument('--features', required=True, nargs='*', 
                        choices=list(EXTRACTER_REGISTRY.keys()),
                        help='A list of features to be extracted')
    parser.add_argument('--gold-path', 
                        help='Path to the gold answer file or the directory '
                        'that contains gold answer files. This option is used '
                        'to generate training data for learning a new model')
    parser.add_argument('--tmp', default='tmp', 
                        help='Path to store temporary files')
    parser.add_argument('--threads', type=int, default=1, 
                        help='number of threads')
    parser.add_argument('--story-suffix', type=str, default='.coref.csv', 
                        help='suffix of story csv filenames')
    parser.add_argument('--char-suffix', type=str, default='.chars', 
                        help='suffix of character list filenames')
    parser.add_argument('--booknlp', type=str, required=True,
                        help='the path to book-nlp')
    parser.add_argument('--svmrank', type=str, required=True,
                        help='the path to svm-rank')
    add_extracter_args(parser)
    args = parser.parse_args()

    data_ciph = os.path.abspath(args.story_path).replace('.', '_').replace('/', '-')
    args.tmp = os.path.join(args.tmp, data_ciph)
    if not os.path.exists(args.tmp):
        os.makedirs(args.tmp)

    print('Running in {} mode ... '.format(args.mode))

    if args.mode == 'predict':
        predict(args)
    elif args.mode == 'train':
        train(args)
        

if __name__ == '__main__':
    main()
