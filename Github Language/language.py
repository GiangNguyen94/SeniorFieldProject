import json

for i in range(0,1):
	repos_json = open("repos/repos_{}.json".format(i)).read()
	repos = json.loads(repos_json)
	
