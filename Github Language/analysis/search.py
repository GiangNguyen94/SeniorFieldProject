import sqlite3
import json
import OAuth
import os
import urllib

import IPython


def search(q, limit=10, sort="", order="desc"):
	url = "https://api.github.com/search/repositories?access_token={}&q={}&sort={}&order={}".format(OAuth.token(),q,sort,order)
	results_json = urllib.urlopen(url).read()
	results = json.loads(results_json)
	total_count = results['total_count']
	items = [item['full_name'].encode("utf-8") for item in results['items'][:limit]]
	return items

def search_raw(q, limit=10, sort="", order="desc"):
	url = "https://api.github.com/search/repositories?access_token={}&q={}&sort={}&order={}".format(OAuth.token(),q,sort,order)
	results_json = urllib.urlopen(url).read()
	results = json.loads(results_json)
	# IPython.embed()
	return {"total_count":results["total_count"], "items":results["items"][:limit]}
