import sqlite3
import json
import OAuth
import os
import urllib
import datetime

def create_report_folder():
	folder_name = "reports/commits{}".format(datetime.datetime.now().strftime("%Y%m%d%H%M%S"))
	if not os.path.exists(folder_name):
		os.makedirs(folder_name)
	return folder_name

def get_from_url(url):
	results = {}
	url = "{}?access_token={}".format(url,OAuth.token())
	commits_json = urllib.urlopen(url).read()
	commits = json.loads(commits_json)
	while type(commits) is list and len(commits)>1:
		for commit in commits:
			results[commit["sha"]]=commit["commit"]["author"]["date"]
		next_url = "{}&sha={}".format(url,commits[-1]["sha"])
		commits_json = urllib.urlopen(next_url).read()
		commits = json.loads(commits_json)
	return results

def report_commits(hit, folder_name):
	subfolder_name = "{}/{}".format(folder_name,hit[0])
	if not os.path.exists(subfolder_name):
		os.makedirs(subfolder_name)
	records = get_from_url(hit[2])
	with open("{}/{}.csv".format(subfolder_name, hit[1]),"w") as f:
		print >> f, "repo_name,time"
		for record in records.values():
			print >> f, "{},{}".format(hit[1],record)


conn = sqlite3.connect('../repos-sm.sqlite')
c = conn.cursor()

c.execute("SELECT owner, name, commits_url FROM repos WHERE name LIKE ?", ('grit',))
report_folder = create_report_folder()
for hit in c.fetchall():
	report_commits(hit, report_folder)
# report_commits(c.fetchone(), report_folder)

c.close()