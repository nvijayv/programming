# Problem: https://www.hackerearth.com/problem/algorithm/akash-and-gcd-1-15/
# Contest link: https://www.hackerearth.com/amazon-hiring-challenge/algorithm/akash-and-gcd-1-15/
# To compute the SUM (gcd(i, n)) for 1 <= i <= n, we do SUM (d * phi(n/d)) for 1 <= d <= n where d | n.
# To compute range sums we use the Binary Indexed Tree here.


MAX_N = 500001
MOD = 1000000007

phi = [i for i in range(MAX_N)]
gcd_sum = [0] * MAX_N


def main():
    preprocess()
    N = int(raw_input().strip())
    numbers = map(int, raw_input().strip().split())
    size = (N << 1) + 2
    bin_idx_tree = [0] * size
    for (idx, n) in enumerate(numbers):
        if gcd_sum[n] >= MOD:
            gcd_sum[n] %= MOD
        update(bin_idx_tree, size, idx+1, gcd_sum[n])
    Q = int(raw_input().strip())
    for q in range(Q):
        op, x, y = raw_input().strip().split()
        x, y = map(int, [x, y])
        if op == 'C':
            print compute(bin_idx_tree, size, x, y)
        else:
            update(bin_idx_tree, size, x, -gcd_sum[numbers[x-1]])
            update(bin_idx_tree, size, x, gcd_sum[y])
            numbers[x-1] = y


def preprocess():
    for i in range(2, MAX_N):
        if phi[i] == i:
            j = i
            while j < MAX_N:
                phi[j] -= phi[j]/i
                j += i
    for i in range(1, MAX_N):
        j, k = i, 1
        while j < MAX_N:
            gcd_sum[j] += phi[k] * i
            j += i
            k += 1


def update(bin_idx_tree, size, idx, val):
    while idx < size:
        bin_idx_tree[idx] += val
        bin_idx_tree[idx] %= MOD
        # if bin_idx_tree[idx] >= MOD:
        #     bin_idx_tree[idx] -= MOD
        idx += (idx & -idx)


def compute(bin_idx_tree, size, st, en):
    ans = compute_helper(bin_idx_tree, en) - compute_helper(bin_idx_tree, st-1)
    if ans < 0:
        ans += MOD
    return ans


def compute_helper(bin_idx_tree, idx):
    sum = 0
    while idx > 0:
        sum += bin_idx_tree[idx]
        if sum >= MOD:
            sum -= MOD
        idx -= (idx & -idx)
    return sum


if __name__ == '__main__':
    main()
