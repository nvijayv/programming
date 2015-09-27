import numpy


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

    feature_train, target_train, init_coeff = get_features(input_train, N, F)


def get_features(input_train, N, F):
    pass


if __name__ == '__main__':
    main()