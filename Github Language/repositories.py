import json
import urllib
import OAuth
import time
import datetime
import os

def save_json(repos, i=0, idir=0):
	if not os.path.exists("repos"):
		os.makedirs("repos")
	if idir > 0:
		if not os.path.exists("repos/range{}".format(idir)):
			os.makedirs("repos/range{}".format(idir))
		with open("repos/range{}/repos_{}.json".format(idir,i),"w") as f:
			print >> f,repos
	else:
		with open("repos/repos_{}.json".format(i),"w") as f:
			print >> f, repos

link = "https://api.github.com/repositories?access_token={}".format(OAuth.token())
# repos_json = urllib.urlopen(link).read()
# repos = json.loads(repos_json)
# save_json(repos_json)

# for i in range(1,4900):
# 	lastseen = repos[-1]["id"]
# 	link2 = link + "&since={}".format(lastseen)
# 	repos_json = urllib.urlopen(link2).read()
# 	repos = json.loads(repos_json)
# 	save_json(repos_json, i)

# print "Last repo: {}".format(repos[-1]["id"])
# last repo is 1324349

# Estimate output size of the following script: 10G
lastseen = 1324349
i = 4900
call_range = range(4901, 30000)


link2 = link + "&since={}".format(lastseen)
repos_json = urllib.urlopen(link2).read()
repos = json.loads(repos_json)
save_json(repos_json, i, 4901)


for i in call_range:
	if i in call_range[0::100]:
		print "{} {}/30000".format(datetime.datetime.now().time(), i)
		idir = i
	lastseen = repos[-1]["id"]
	link2 = link + "&since={}".format(lastseen)
	repos_json = urllib.urlopen(link2).read()
	repos = json.loads(repos_json)
	save_json(repos_json, i, idir)
	time.sleep(0.72)






