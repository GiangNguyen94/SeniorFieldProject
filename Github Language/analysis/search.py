import sqlite3
import json
import OAuth
import os
import urllib

import IPython


def search(q, limit=10, sort="", order=""):
	url = "https://api.github.com/search/repositories?access_token={}&q={}&sort={}&order={}".format(OAuth.token(),q,sort,order)
	results_json = urllib.urlopen(url).read()
	results = json.loads(results_json)
	total_count = results['total_count']
	items = [item['full_name'].encode("utf-8") for item in results['items'][:limit]]
	return items


# stargazers

# language q=language:xxx

# users with most repos(easy)
# 10 repos with most stars #include history
# owners and contributers of the 10 repos with most stars
# find contributors of a repo, repositories/1/contributors

# the top 30 java repos with the most stars (search/repositories?q=language:Java&sort=stars)