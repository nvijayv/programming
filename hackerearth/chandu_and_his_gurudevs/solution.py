# Problem: https://www.hackerearth.com/problem/algorithm/chandu-and-his-gurudevs-1/
# Contest link: https://www.hackerearth.com/amazon-hiring-challenge/algorithm/chandu-and-his-gurudevs-1/
# Using the Set-Union-Find approach and inserting edges of the tree in sorted order of weight to find
# SUM F(u, v), where F(u, v) = max wt among edges on the path between vertices u & v

MOD = 1000000007


def main():
    T = int(raw_input().strip())
    for __ in range(T):
        N = int(raw_input().strip())
        edges = []
        sizes = [1] * N
        parents = [i for i in range(N)]
        for __ in range(N-1):
            u, v, wt = map(int, raw_input().strip().split())
            edges.append((wt, (u-1, v-1)))
        edges.sort()

        sum = 0
        for e in edges:
            wt, (u, v) = e
            u = get_root(parents, u)
            v = get_root(parents, v)
            sum = (sum + wt * sizes[u] * sizes[v]) % MOD
            if sizes[u] > sizes[v]:
                parents[v] = u
                sizes[u] += sizes[v]
            else:
                parents[u] = v
                sizes[v] += sizes[u]
        print sum


def get_root(parents, v):
    while parents[v] != v:
        v = parents[v]
    return v


if __name__ == '__main__':
    main()