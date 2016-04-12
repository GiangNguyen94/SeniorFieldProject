import sqlite3
import json
import OAuth
import os
import urllib
import datetime
import dbservice
import IPython

def create_report_folder():
	folder_name = "reports/forks{}".format(datetime.datetime.now().strftime("%Y%m%d%H%M%S"))
	if not os.path.exists(folder_name):
		os.makedirs(folder_name)
	return folder_name

def get_from_url(url):
	results = {}
	url = "{}?access_token={}&per_page=100".format(url,OAuth.token())
	page=1
	next_url = "{}&page={}".format(url,page)
	forks_json = urllib.urlopen(next_url).read()
	forks = json.loads(forks_json)
	while type(forks) is list and len(forks)>0:		
		for fork in forks:
			results[fork["id"]]=[fork["id"], fork["created_at"]]
		page += 1
		next_url = "{}&page={}".format(url,page)
		forks_json = urllib.urlopen(next_url).read()
		forks = json.loads(forks_json)
	return results

def report_forks(hit, folder_name):
	subfolder_name = "{}/{}".format(folder_name,hit[0])
	if not os.path.exists(subfolder_name):
		os.makedirs(subfolder_name)
	records = get_from_url(hit[2]).values()
	sorted_records = sorted(records, key=lambda l:l[1]) #sort by fork id
	with open("{}/{}.csv".format(subfolder_name, hit[1]),"w") as f:
		print >> f, "repo_name,time,id"
		for record in sorted_records:
			print >> f, "{},{},{}".format(hit[1],record[1],record[0])



def find_by_name(name, exact=True, limit=10):
	c = dbservice.get_cursor()
	if exact:
		c.execute("SELECT owner, name, forks_url FROM repos WHERE name LIKE ?", (name,))
	else:
		c.execute("SELECT owner, name, forks_url FROM repos WHERE name LIKE ?", ("%{}%".format(name),))
	report_folder = create_report_folder()
	for hit in c.fetchall()[:limit]:
		report_forks(hit, report_folder)
	print "Completed. Find reports in {}".format(report_folder)
	c.close()

def find_by_full_name(full_name):
	c = dbservice.get_cursor()
	c.execute("SELECT owner, name, forks_url FROM repos WHERE full_name LIKE ?", (full_name,))
	report_folder = create_report_folder()
	for hit in c.fetchall():
		report_forks(hit, report_folder)
	print "Completed. Find reports in {}".format(report_folder)
	c.close()

def find_by_full_names(full_name_list):
	c = dbservice.get_cursor()
	report_folder = create_report_folder()
	for name in full_name_list:
		c.execute("SELECT owner, name, forks_url FROM repos WHERE full_name LIKE ?", (name,))
		hits = c.fetchall()
		IPython.embed()
		for hit in hits:
			report_forks(hit, report_folder)
	print "Completed. Find reports in {}".format(report_folder)
	c.close()



# Usage examples:
# find_by_name('grit', exact=True, limit=3)
# find_by_name('grit', exact=False, limit=30)
# find_by_full_name('mojombo/grit')
# find_by_full_names(['darrenboyd/grit', 'foca/integrity-email', 'foca/integrity'])