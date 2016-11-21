from collections import defaultdict
import json
import re
import sys
import math
import operator


training_file_path = 'training.json'
alpha = 1.0     # smoothing parameter
BASE_2 = 2      # logarithm base 2

non_char_dig_pat = re.compile(r"[^0-9a-zA-Z]")
stopwords = frozenset([
    "a", "about", "above", "across", "after", "afterwards", "again", "against",
    "all", "almost", "alone", "along", "already", "also", "although", "always",
    "am", "among", "amongst", "amoungst", "amount", "an", "and", "another",
    "any", "anyhow", "anyone", "anything", "anyway", "anywhere", "are",
    "around", "as", "at", "back", "be", "became", "because", "become",
    "becomes", "becoming", "been", "before", "beforehand", "behind", "being",
    "below", "beside", "besides", "between", "beyond", "bill", "both",
    "bottom", "but", "by", "call", "can", "cannot", "cant", "co", "con",
    "could", "couldnt", "cry", "de", "describe", "detail", "do", "done",
    "down", "due", "during", "each", "eg", "eight", "either", "eleven", "else",
    "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone",
    "everything", "everywhere", "except", "few", "fifteen", "fify", "fill",
    "find", "fire", "first", "five", "for", "former", "formerly", "forty",
    "found", "four", "from", "front", "full", "further", "get", "give", "go",
    "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter",
    "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his",
    "how", "however", "hundred", "i", "ie", "if", "in", "inc", "indeed",
    "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter",
    "latterly", "least", "less", "ltd", "made", "many", "may", "me",
    "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly",
    "move", "much", "must", "my", "myself", "name", "namely", "neither",
    "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone",
    "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on",
    "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our",
    "ours", "ourselves", "out", "over", "own", "part", "per", "perhaps",
    "please", "put", "rather", "re", "same", "see", "seem", "seemed",
    "seeming", "seems", "serious", "several", "she", "should", "show", "side",
    "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone",
    "something", "sometime", "sometimes", "somewhere", "still", "such",
    "system", "take", "ten", "than", "that", "the", "their", "them",
    "themselves", "then", "thence", "there", "thereafter", "thereby",
    "therefore", "therein", "thereupon", "these", "they", "thick", "thin",
    "third", "this", "those", "though", "three", "through", "throughout",
    "thru", "thus", "to", "together", "too", "top", "toward", "towards",
    "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us",
    "very", "via", "was", "we", "well", "were", "what", "whatever", "when",
    "whence", "whenever", "where", "whereafter", "whereas", "whereby",
    "wherein", "whereupon", "wherever", "whether", "which", "while", "whither",
    "who", "whoever", "whole", "whom", "whose", "why", "will", "with",
    "within", "without", "would", "yet", "you", "your", "yours", "yourself",
    "yourselves"])

topics = frozenset([u'gis', u'security', u'photo', u'mathematica', u'unix', u'wordpress', u'scifi', u'electronics', u'android', u'apple'])

freq_words_by_topic = dict()
"""
count_unigrams_by_topic = defaultdict(int)
count_bigrams_by_topic = defaultdict(int)
"""
count_words_by_topic = defaultdict(int)
freq_topics = defaultdict(int)
# word_occur_count_by_docs = defaultdict(int)
"""
unique_unigrams = set()
unique_bigrams = set()
"""
unique_words = set()

def main():
    for topic in topics:
        freq_words_by_topic[topic] = defaultdict(int)

    ### Training ###
    training_size = -1
    with open(training_file_path, 'r') as training_file:
        for line in training_file:
            if training_size < 0:
                training_size = int(line.strip())
                continue
            line_map = json.loads(line.strip().lower())
            topic = line_map['topic'].strip()
            question = line_map['question'].strip()
            excerpt = line_map['excerpt'].strip()
            q_tokens, len_q = tokenize(question)
            e_tokens, len_e = tokenize(excerpt)

            freq_topics[topic] += 1
            for i in range(len_q):
                freq_words_by_topic[topic][q_tokens[i]] += 1
                """
                count_unigrams_by_topic[topic] += 1
                unique_unigrams.add(q_tokens[i])
                if i < len_q-1:
                    bigram = q_tokens[i] + " " + q_tokens[i+1]
                    freq_words_by_topic[topic][bigram] += 1
                    count_bigrams_by_topic[topic] += 1
                    unique_bigrams.add(bigram)
                """
                count_words_by_topic[topic] += 1
                unique_words.add(q_tokens[i])

            for i in range(len_e):
                freq_words_by_topic[topic][e_tokens[i]] += 1
                """
                count_unigrams_by_topic[topic] += 1
                unique_unigrams.add(e_tokens[i])
                if i < len_e-1:
                    bigram = e_tokens[i] + " " + e_tokens[i+1]
                    freq_words_by_topic[topic][bigram] += 1
                    count_bigrams_by_topic[topic] += 1
                    unique_bigrams.add(bigram)
                """
                count_words_by_topic[topic] += 1
                unique_words.add(e_tokens[i])

            """
            for tok in set(q_tokens + e_tokens):
                word_occur_count_by_docs[tok] += 1
            """

    print >> sys.stderr, 'Done training with', training_size, 'examples'

    ### Testing ###
    num_tests = int(raw_input().strip())
    for t in range(num_tests):
        line_map = json.loads(raw_input().strip().lower())
        question = line_map['question'].strip()
        excerpt = line_map['excerpt'].strip()
        q_tokens, len_q = tokenize(question)
        e_tokens, len_e = tokenize(excerpt)

        cand_ans = dict()
        for topic in freq_topics:
            cand_ans[topic] = math.log(freq_topics[topic] * 1.0/training_size)
        for topic in freq_words_by_topic:
            """
            for word in q_tokens:
                log_prob_word_given_topic = math.log((freq_words_by_topic[topic][word]+alpha) / (count_unigrams_by_topic[topic] + alpha * len(unique_unigrams)))
                cand_ans[topic] += log_prob_word_given_topic * (math.log((training_size + 1.0) / (word_occur_count_by_docs[word]+1.0), BASE_2) + 1.0)
            """
            for i in range(len_q):
                # log_prob_unigram_given_topic = math.log((freq_words_by_topic[topic][q_tokens[i]] + alpha) / (count_unigrams_by_topic[topic] + alpha * len(unique_unigrams)))
                log_prob_unigram_given_topic = math.log((freq_words_by_topic[topic][q_tokens[i]] + alpha) / (count_words_by_topic[topic] + alpha * len(unique_words)))
                cand_ans[topic] += log_prob_unigram_given_topic
                if i < len_q-1:
                    # bigram = q_tokens[i] + " " + q_tokens[i+1]
                    # log_prob_bigram_given_topic = math.log((freq_words_by_topic[topic][bigram]+alpha) / (count_bigrams_by_topic[topic] + alpha * len(unique_bigrams)))
                    bigram_prob = freq_words_by_topic[topic][q_tokens[i]] * freq_words_by_topic[topic][q_tokens[i+1]] * 1.0 / (count_words_by_topic[topic] ** 2)
                    log_prob_bigram_given_topic =
                    cand_ans[topic] += log_prob_bigram_given_topic
            """
            for word in e_tokens:
                log_prob_word_given_topic = math.log((freq_words_by_topic[topic][word]+alpha) / (count_unigrams_by_topic[topic] + alpha * len(unique_unigrams)))
                cand_ans[topic] += log_prob_word_given_topic * (math.log((training_size + 1.0) / (word_occur_count_by_docs[word]+1.0), BASE_2) + 1.0)
            """
            for i in range(len_e):
                log_prob_unigram_given_topic = math.log((freq_words_by_topic[topic][e_tokens[i]] + alpha) / (count_unigrams_by_topic[topic] + alpha * len(unique_unigrams)))
                cand_ans[topic] += log_prob_unigram_given_topic
                if i < len_e-1:
                    bigram = e_tokens[i] + " " + e_tokens[i+1]
                    log_prob_bigram_given_topic = math.log((freq_words_by_topic[topic][bigram]+alpha) / (count_bigrams_by_topic[topic] + alpha * len(unique_bigrams)))
                    cand_ans[topic] += log_prob_bigram_given_topic

        print max(cand_ans.iteritems(), key=operator.itemgetter(1))[0]


def tokenize(line):
    line = non_char_dig_pat.sub(' ', line.lower())
    tokens = [word for word in line.split() if word not in stopwords]
    return tokens, len(tokens)


if __name__ == '__main__':
    main()