import sys

TOKENS = ["c8e0df7a7c5e9b967e76e705d32883fccc60738f", \
			"c818af58573556e73e4d059f02fd347bf07e8dee"]
n_tokens = len(TOKENS)

count = 0

# returns a token
def token():
	global count
	count = count%sys.maxint+1
	i = count % n_tokens
	return TOKENS[i]