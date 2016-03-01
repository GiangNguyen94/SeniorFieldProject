import json
import urllib
import OAuth

def save_json(repos, i=0):
	f = open("repos/repos_{}.json".format(i),"w")
	print >> f, repos

link = "https://api.github.com/repositories?access_token={}".format(OAuth.token())
repos_json = urllib.urlopen(link).read()
repos = json.loads(repos_json)
save_json(repos_json)

for i in range(1,3):
	lastseen = repos[-1]["id"]
	link2 = link + "&since={}".format(lastseen)
	repos_json = urllib.urlopen(link2).read()
	repos = json.loads(repos_json)
	# save(repos)
	save_json(repos_json, i)