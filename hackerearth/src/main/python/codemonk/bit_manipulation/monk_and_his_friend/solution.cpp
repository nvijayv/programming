// Problem: https://www.hackerearth.com/code-monk-bit-manipulation/algorithm/monk-and-his-friend/

#include <iostream>
using namespace std;

int countSetBits(long long num) {
	int count = 0;
	while (num > 0LL) {
		count++;
		num = num & (num - 1LL);
	}
	return count;
}

int main()
{
	int T;
	long long M, P;
	scanf(" %d", &T);
	while (T--) {
		scanf(" %lld %lld", &M, &P);
		printf("%d\n", countSetBits(M ^ P));
	}
	return 0;
}

