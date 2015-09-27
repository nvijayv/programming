import numpy


alpha = 0.1
num_iters = 10000
epsilon = 1


def main():
    F, N = map(int, raw_input().strip().split())
    input_train = []
    for i in range(N):
        point = map(float, raw_input().strip().split())
        assert len(point) == F+1
        input_train.append(point)

    T = int(raw_input().strip())
    input_test = []
    for t in range(T):
        point = map(float, raw_input().strip().split())
        assert len(point) == F
        input_test.append(point)

    # local_train_data = input_train[:60]
    # local_test_data = input_train[60:]
    # local_test_feature = [test_datum[:-1] for test_datum in local_test_data]
    # local_test_target = [test_datum[-1] for test_datum in local_test_data]
    #
    # feature_train, target_train, init_coeff = get_features(local_train_data)
    # theta, J_history = gradient_descent(feature_train, target_train, init_coeff, alpha, num_iters)
    # print 'J_history:'
    # for cost in J_history:
    #     print 'cost:', cost
    #
    # local_test_pred = get_predictions(local_test_feature, theta)
    # accuracy = get_accuracy(local_test_pred, local_test_target, epsilon)
    # print 'accuracy:', accuracy, '%'

    feature_train, target_train, init_coeff = get_features(input_train)
    theta, J_history = gradient_descent(feature_train, target_train, init_coeff, alpha, num_iters)
    predictions = get_predictions(input_test, theta)
    for pred in predictions:
        print '%.2f' % pred


def get_features(input_training_data):
    feature_list = []
    target_list = []
    for row in input_training_data:
        feature = create_feature(row[:-1])
        feature_list.append(feature)
        target_list.append(row[-1])
    feature_train = numpy.array(feature_list)
    target_train = numpy.array(target_list).reshape((feature_train.shape[0], 1))
    init_coeff = numpy.zeros(shape=(feature_train.shape[1], 1))
    return feature_train, target_train, init_coeff


def create_feature(point):
    # Adding all features with degree < 4 as stated in the problem
    feature = [1]
    feature += [x for x in point]
    feature += [x1 * x2 for x1 in point for x2 in point]
    feature += [x1 * x2 * x3 for x1 in point for x2 in point for x3 in point]
    return feature


def gradient_descent(X, y, theta, alpha, num_iters):
    """
    Performs Gradient Descent to learn theta
    by taking num_iters steps with learning rate alpha
    :param X: features
    :param y: target values
    :param theta: coefficients for Linear Regression with some initial values, to be learned during the algorithm
    :param alpha: learning rate
    :param num_iters: number of steps
    :return: learned value of theta and a history of cost values for all the steps
    """

    m = y.size
    J_history = numpy.zeros(shape=(num_iters, 1))
    for i in range(num_iters):
        predictions = X.dot(theta)
        theta = theta - (alpha/m) * ((predictions - y).T.dot(X)).T
        J_history[i, 0] = get_cost(X, y, theta)

    return theta, J_history


def get_cost(X, y, theta):
    """
    :param X: features
    :param y: target values
    :param theta: coefficients for Linear Regression
    :return: the cost or error for Linear Regression
    """
    m = y.size
    predictions = X.dot(theta)
    sq_errors = (predictions - y) ** 2
    J = 1.0 / (2.0 * m) * sq_errors.sum()
    return J


def get_predictions(input_test, theta):
    predictions = []
    for point in input_test:
        feature_test = create_feature(point)
        point_vector = numpy.array(feature_test).reshape((len(feature_test), 1))
        predictions.append(theta.T.dot(point_vector)[0][0])
    return predictions


def get_accuracy(predictions, true_values, epsilon):
    total = len(predictions)
    matched = sum([1 if abs(pred - tr_val) <= epsilon else 0 for (pred, tr_val) in zip(predictions, true_values)])
    return matched * 100.0 / total


if __name__ == '__main__':
    main()