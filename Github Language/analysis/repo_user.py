import search
import datetime
import os
import OAuth
import urllib
import json
import IPython

def create_report_folder():
	folder_name = "reports/users{}".format(datetime.datetime.now().strftime("%Y%m%d%H%M%S"))
	if not os.path.exists(folder_name):
		os.makedirs(folder_name)
	return folder_name

def report_contributor(item, folder_name):
	subfolder_name = "{}/{}".format(folder_name,item["owner"]["login"])
	if not os.path.exists(subfolder_name):
		os.makedirs(subfolder_name)
	repo_id = item["id"]
	url = "https://api.github.com/repositories/{}/contributors?access_token={}".format(repo_id,OAuth.token())
	contributors_json = urllib.urlopen(url).read()
	# IPython.embed()
	if len(contributors_json) > 0:
		contributors = json.loads(contributors_json)
		# IPython.embed()
		records = [[c["login"],c["contributions"]] for c in contributors]
		with open("{}/{}.csv".format(subfolder_name, item["name"]),"w") as f:
			print >> f, "username,contributions"
			for record in records:
				print >> f, "{},{}".format(record[0],record[1])

def report_contributors(items, folder_name):
	for item in items:
		report_contributor(item, folder_name)

def report_owners(items, folder_name):
	records = [[item["owner"]["login"],item["name"],item["stargazers_count"],item["forks_count"]] for item in items]
	folder_name = create_report_folder()
	with open("{}/owners.csv".format(folder_name), "w") as f:
		print >> f, "owner,repo,stars,forks"
		for record in records:
			print >> f, "{},{},{},{}".format(record[0],record[1],record[2],record[3])


def find_live_by_search_query(q, limit=10, sort="", order="desc", report_type=["owner","contributors"]):
	results = search.search_raw(q, limit=limit, sort=sort, order=order)
	folder_name = create_report_folder()
	if "owner" in report_type:
		report_owners(results["items"][:limit], folder_name)
	if "contributors" in report_type:
		report_contributors(results["items"][:limit], folder_name)