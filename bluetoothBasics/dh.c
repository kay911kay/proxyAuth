/*
*    Diffie Hellman implementation sourced from: https://gist.github.com/cloudwu/8838724
*/

// The biggest 64bit prime
#define P 0xffffffffffffffc5ull
#define G 5


#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include "dh.h"

// calc a * b % p , avoid 64bit overflow
static inline uint64_t mul_mod_p(uint64_t a, uint64_t b) {
	uint64_t m = 0;
	while(b) {
		if(b&1) {
			uint64_t t = P-a;
			if ( m >= t) {
				m -= t;
			} else {
				m += a;
			}
		}
		if (a >= P - a) {
			a = a * 2 - P;
		} else {
			a = a * 2;
		}
		b>>=1;
	}
	return m;
}

static inline uint64_t pow_mod_p(uint64_t a, uint64_t b) {
	if (b==1) {
		return a;
	}
	uint64_t t = pow_mod_p(a, b>>1);
	t = mul_mod_p(t,t);
	if (b % 2) {
		t = mul_mod_p(t, a);
	}
	return t;
}

// calc a^b % p
uint64_t powmodp(uint64_t a, uint64_t b) {
	if (a > P)
		a%=P;
	return pow_mod_p(a,b);
}
