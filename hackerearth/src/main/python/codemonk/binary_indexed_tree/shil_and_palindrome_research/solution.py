# Problem: https://www.hackerearth.com/code-monk-binary-indexed-tree/algorithm/shil-and-palindrome-research/


def main():
    N, Q = map(int, raw_input().strip().split())
    size = (N << 1) + 2
    bin_idx_tree = []
    for __ in range(size):
        bin_idx_tree.append(dict())
    S = list(raw_input().strip())
    for (idx, ch) in enumerate(S):
        update(bin_idx_tree, size, idx+1, ch, 1)

    for __ in range(Q):
        code, L, xx = raw_input().strip().split()
        code, L = map(int, [code, L])
        if int(code) == 1:
            update(bin_idx_tree, size, L, S[L-1], -1)
            update(bin_idx_tree, size, L, xx, 1)
            S[L-1] = xx
        else:
            R = int(xx)
            print check(bin_idx_tree, size, L, R)


def update(bin_idx_tree, size, idx, ch, val):
    while idx < size:
        if ch not in bin_idx_tree[idx]:
            bin_idx_tree[idx][ch] = 0
        bin_idx_tree[idx][ch] += val
        idx += (idx & -idx)


def check(bin_idx_tree, size, L, R):
    l_cnts = compute(bin_idx_tree, L-1)
    r_cnts = compute(bin_idx_tree, R)
    counts = [0] * 26
    for ch in r_cnts:
        counts[ord(ch) - ord('a')] = r_cnts[ch]
    for ch in l_cnts:
        counts[ord(ch) - ord('a')] -= l_cnts[ch]

    odd_cnt = 0
    for cnt in counts:
        if cnt % 2 == 1:
            odd_cnt += 1
    if odd_cnt > 1:
        return 'no'
    return 'yes'


def compute(bin_idx_tree, idx):
    counts = dict()
    while idx > 0:
        idx_cnts = bin_idx_tree[idx]
        for ch in idx_cnts:
            if ch not in counts:
                counts[ch] = 0
            counts[ch] += idx_cnts[ch]
        idx -= (idx & -idx)
    return counts


if __name__ == '__main__':
    main()