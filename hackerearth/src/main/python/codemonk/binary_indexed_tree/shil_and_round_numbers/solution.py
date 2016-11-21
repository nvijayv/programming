# Problem: https://www.hackerearth.com/code-monk-binary-indexed-tree/algorithm/shil-and-round-numbers/


def main():
    N, Q = map(int, raw_input().strip().split())
    numbers = map(int, raw_input().strip().split())
    size = (N << 1) + 2
    bin_idx_tree = [0] * size
    for (idx, num) in enumerate(numbers):
        update(bin_idx_tree, size, idx+1, num, 1)
    for __ in range(Q):
        code, x, y = map(int, raw_input().strip().split())
        if code == 1:
            print compute(bin_idx_tree, size, x, y)
        else:
            update(bin_idx_tree, size, x, numbers[x-1], -1)
            update(bin_idx_tree, size, x, y, 1)
            numbers[x-1] = y


def update(bin_idx_tree, size, idx, num, val):
    if not is_round(num):
        return
    while idx < size:
        bin_idx_tree[idx] += val
        idx += (idx & -idx)


def compute(bin_idx_tree, size, l, r):
    return compute_helper(bin_idx_tree, r) - compute_helper(bin_idx_tree, l-1)


def compute_helper(bin_idx_tree, idx):
    sum = 0
    while idx > 0:
        sum += bin_idx_tree[idx]
        idx -= (idx & -idx)
    return sum


def is_round(num):
    if num < 0:
        return False
    if num < 10:
        return True
    string_num = str(num)
    first_digit = string_num[0]
    last_digit = string_num[-1]
    return first_digit == last_digit


if __name__ == '__main__':
    main()