import json
import numpy
import sklearn.feature_extraction
import sklearn.naive_bayes


training_file_path = 'training.json'
train_documents = []
train_labels = []
test_documents = []

label_to_int = dict()
int_to_label = dict()

def main():
    ### Training ###
    training_size = -1
    with open(training_file_path, 'r') as training_file:
        lineno = 0
        lbl_count = 0
        for line in training_file:
            lineno += 1
            if training_size < 0:
                training_size = int(line.strip())
                continue
            line_map = json.loads(line.strip().lower())
            topic = line_map['topic'].strip()
            question = line_map['question'].strip()
            excerpt = line_map['excerpt'].strip()
            # train_documents.append(question + " " + excerpt)
            train_documents.append(" ".join([question] * 4 + [excerpt]))
            if topic not in label_to_int:
                label_to_int[topic] = lbl_count
                int_to_label[lbl_count] = topic
                lbl_count += 1
            train_labels.append(label_to_int[topic])

    count_vectorizer = sklearn.feature_extraction.text.CountVectorizer(ngram_range=(1, 2), stop_words='english', token_pattern=r'\b\w+\b')
    counts = count_vectorizer.fit_transform(train_documents)

    tfidf_vectorizer = sklearn.feature_extraction.text.TfidfTransformer(sublinear_tf=True)
    tfidf = tfidf_vectorizer.fit_transform(counts)

    nb = sklearn.naive_bayes.MultinomialNB()
    model = nb.fit(tfidf, train_labels)

    ### Testing ###
    N = int(raw_input().strip())
    for n in range(N):
        line_map = json.loads(raw_input().strip().lower())
        question = line_map['question'].strip()
        excerpt = line_map['excerpt'].strip()
        # test_documents.append(question + " " + excerpt)
        test_documents.append(" ".join([question] * 4 + [excerpt]))

    test_count = count_vectorizer.transform(test_documents)
    test_tfidf = tfidf_vectorizer.transform(test_count)
    predicted = model.predict(test_tfidf)
    for lbl_num in predicted:
        print int_to_label[lbl_num]


if __name__ == '__main__':
    main()