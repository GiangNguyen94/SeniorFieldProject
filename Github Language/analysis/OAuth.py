import sys
import os

filename = os.path.join(os.path.dirname(__file__), 'access_token.secret')
with open(filename) as f:
	TOKENS = [line.rstrip() for line in f]

n_tokens = len(TOKENS)
count = 0

# returns a token
def token():
	global count
	count = count%sys.maxint+1
	i = count % n_tokens
	return TOKENS[i]