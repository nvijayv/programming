// Problem: https://www.hackerearth.com/code-monk-bit-manipulation/algorithm/monks-choice-of-numbers-1/

#include <iostream>
#include <algorithm>
#include <vector>
using namespace std;

int countSetBits(int num) {
	int count = 0;
	while (num > 0) {
		count++;
		num = num & (num - 1);
	}
	return count;
}

int main()
{
	int T, N, K, a, sum;
	vector<int> numOnes;
	scanf(" %d", &T);
	while (T--) {
		scanf(" %d %d", &N, &K);
		numOnes.clear();
		sum = 0;
		for (int j = 0; j < N; j++) {
			scanf(" %d", &a);
			numOnes.push_back(countSetBits(a));
		}
		sort(numOnes.begin(), numOnes.end());
		for (int i = N-1; i>= N-K; i--) {
			sum += numOnes[i];
		}
		printf("%d\n", sum);
	}
	return 0;
}

