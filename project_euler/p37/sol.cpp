#include <cstdio>
#include <vector>

using namespace std;

#define MAXNUM 1000000

bool isPrime[MAXNUM];
vector<int> primes;

void sieve(int N) {
    isPrime[0] = isPrime[1] = false;
    for (int i=2; i<N; i++) isPrime[i] = true;

    primes.push_back(2);
    for (int j=2; 2*j<N; j++)
        isPrime[2*j] = false;

    for (int i=3; i<N; i+=2) {
        if (not isPrime[i]) continue;
        primes.push_back(i);
        for (int j=2; i*j<N; j++)
            isPrime[i*j] = false;
    }
}

bool check(int prime) {
    bool good = true;
    int p = prime, place = 1000000;
    // Right truncation checks
    while (p) {
        if (not isPrime[p]) return false;
        p /= 10;
    }
    // Left truncation checks
    p = prime;
    while (p/place == 0) place /= 10;
    while (p) {
        if (not isPrime[p]) return false;
        int msb = p/place;
        p -= msb*place;
        place /= 10;
    }
    return true;
}

int main() {
    long long sum = 0LL;
    sieve(MAXNUM);

    for (int i=4; i<primes.size(); i++) {
        if (check(primes[i])) {
            printf("%d\n", primes[i]);
            sum += primes[i];
        }
    }
    printf("sum = %lld\n", sum);
    return 0;
}

