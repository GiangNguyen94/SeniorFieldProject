import sys

with open('access_token.secret') as f:
	TOKENS = [line.rstrip() for line in f]

n_tokens = len(TOKENS)
count = 0

# returns a token
def token():
	global count
	count = count%sys.maxint+1
	i = count % n_tokens
	return TOKENS[i]