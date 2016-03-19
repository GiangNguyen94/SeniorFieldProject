import json
import urllib
import OAuth
import time
import datetime
import os

def save_json(repos, idir=0, ifile=0):
	if not os.path.exists("repos"):
		os.makedirs("repos")
	if idir > 0:
		if not os.path.exists("repos/part{}".format(idir)):
			os.makedirs("repos/part{}".format(idir))
		with open("repos/part{}/repos_{}.json".format(idir,ifile),"w") as f:
			print >> f,repos
	else:
		with open("repos/repos_{}.json".format(ifile),"w") as f:
			print >> f, repos

link = "https://api.github.com/repositories?access_token={}".format(OAuth.token())

def run_in_range_since(folder_range, lastseen, file_range = range(0,100)):
	for idir in folder_range:
		print "{} {}/600".format(datetime.datetime.now().time(), idir)	
		for ifile in file_range:
			link2 = link + "&since={}".format(lastseen)
			repos_json = urllib.urlopen(link2).read()
			repos = json.loads(repos_json)
			if len(repos)==0:
				return idir, idir*100+ifile-1, lastseen
			save_json(repos_json, idir, idir*100+ifile)
			lastseen = repos[-1]["id"]
	return idir, idir*100+ifile, lastseen


# Estimate output size of the following script: 12G
lastseen = 6945078
folder_range = range(301, 600)

last_dir, last_file, last_repo = run_in_range_since(folder_range, lastseen)
with open("api_repo_download.log","w") as f:
	print >> f, json.dumps({"last_dir":last_dir, "last_file":last_file, "last_repo":last_repo})



