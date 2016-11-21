# Problem: https://www.hackerearth.com/problem/algorithm/akash-and-gcd-1-15/
# Contest link: https://www.hackerearth.com/amazon-hiring-challenge/algorithm/akash-and-gcd-1-15/
# To compute the SUM (gcd(i, n)) for 1 <= i <= n, we do SUM (d * phi(n/d)) for 1 <= d <= n where d | n.
# Here we use a Segment Tree to compute range sums but sadly it times out on 4 out of 9 test cases.

MAX_N = 500001
MOD = 1000000007

phi = [i for i in range(MAX_N)]
gcd_sum = [0] * MAX_N


def main():
    preprocess()
    N = int(raw_input().strip())
    numbers = map(int, raw_input().strip().split())
    seg_tree = [0] * (MAX_N * 5 + 10)
    for (idx, n) in enumerate(numbers):
        if gcd_sum[n] >= MOD:
            gcd_sum[n] %= MOD
        update(seg_tree, N, idx, gcd_sum[n])
    Q = int(raw_input().strip())
    for q in range(Q):
        op, x, y = raw_input().strip().split()
        x, y = map(int, [x, y])
        if op == 'C':
            print compute(seg_tree, N, x-1, y-1)
        else:
            update(seg_tree, N, x-1, gcd_sum[y])


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


def update(seg_tree, N, idx, val):
    update_helper(seg_tree, 1, 0, N-1, idx, val)


def update_helper(seg_tree, pt, st, en, idx, val):
    if st > en:
        return
    if st == en:
        seg_tree[pt] = val
        return
    mid = (st + en) >> 1
    if idx <= mid:
        update_helper(seg_tree, pt << 1, st, mid, idx, val)
    else:
        update_helper(seg_tree, (pt << 1) + 1, mid+1, en, idx, val)
    seg_tree[pt] = seg_tree[pt << 1] + seg_tree[(pt << 1) + 1]
    if seg_tree[pt] >= MOD:
        seg_tree[pt] -= MOD


def compute(seg_tree, N, st, en):
    return compute_helper(seg_tree, 1, 0, N-1, st, en)


def compute_helper(seg_tree, pt, st, en, x, y):
    if st > en or en < x or y < st:
        return 0
    if st == en:
        return seg_tree[pt]
    mid = st + (en-st)/2
    ans = compute_helper(seg_tree, pt << 1, st, mid, x, y) + compute_helper(seg_tree, (pt << 1) + 1, mid+1, en, x, y)
    if ans >= MOD:
        ans -= MOD
    return ans


if __name__ == '__main__':
    main()
