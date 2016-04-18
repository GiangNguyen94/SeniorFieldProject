import sqlite3
import json
import OAuth
import os
import urllib
import datetime
import dbservice

def create_report_folder():
	folder_name = "reports/commits{}".format(datetime.datetime.now().strftime("%Y%m%d%H%M%S"))
	if not os.path.exists(folder_name):
		os.makedirs(folder_name)
	return folder_name

def get_from_url(url):
	results = {}
	url = "{}?per_page=100".format(url)
	commits_json = urllib.urlopen(url).read()
	commits = json.loads(commits_json)
	while type(commits) is list and len(commits)>1:
		for commit in commits:
			results[commit["sha"]]=commit["commit"]["author"]["date"]
		next_url = "{}&access_token={}&sha={}".format(url,OAuth.token(),commits[-1]["sha"])
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

def find_raw(username, reponame, url, folder=None):
	if folder is None:
		folder = create_report_folder()
	report_commits([username, reponame, url], folder)

def find_raws(params):
	folder = create_report_folder()
	for param in params:
		report_commits(param, folder)

def find_by_name(name, exact=True, limit=10):
	c = dbservice.get_cursor()
	if exact:
		c.execute("SELECT owner, name, commits_url FROM repos WHERE name LIKE ?", (name,))
	else:
		c.execute("SELECT owner, name, commits_url FROM repos WHERE name LIKE ?", ("%{}%".format(name),))
	report_folder = create_report_folder()
	for hit in c.fetchall()[:limit]:
		report_commits(hit, report_folder)
	print "Completed. Find reports in {}".format(report_folder)
	c.close()

def find_by_full_name(full_name):
	c = dbservice.get_cursor()
	c.execute("SELECT owner, name, commits_url FROM repos WHERE full_name LIKE ?", (full_name,))
	report_folder = create_report_folder()
	for hit in c.fetchall():
		report_commits(hit, report_folder)
	print "Completed. Find reports in {}".format(report_folder)
	c.close()

def find_by_full_names(full_name_list):
	c = dbservice.get_cursor()
	report_folder = create_report_folder()
	for name in full_name_list:
		c.execute("SELECT owner, name, commits_url FROM repos WHERE full_name LIKE ?", (name,))
		for hit in c.fetchall():
			report_commits(hit, report_folder)
	print "Completed. Find reports in {}".format(report_folder)
	c.close()