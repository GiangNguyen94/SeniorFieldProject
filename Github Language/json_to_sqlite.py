import sqlite3
import json

conn = sqlite3.connect('repos.sqlite')
c = conn.cursor()

## repos_0, create table
with open("repos/repos_0.json") as f:
	js = f.read()

repos = json.loads(js)
current_keys = repos[0].keys()

attrs = ",".join(map(lambda s: s+" text",current_keys))
c.execute("DROP TABLE IF EXISTS repos")
c.execute("CREATE TABLE repos ({})".format(attrs)) # Possible SQL injection

for repo in repos:
	for key in repo.keys():
		c.execute("INSERT INTO repos({}) VALUES(?)".format(key), (unicode(repo[key]),))
	
for repo in repos:
	Insert id, id
	for key in repo.keys():
		if key not id:
			Updeate id=id (key) (value)



for repo in repos:
	Insert into repos(key,key,k,k,k,k....) values( values, values, vlaues,vvd....)

conn.commit()

# ## repos_1-4899
# for i in range(1,4900):
# 	with open("repos/repos_{}.json".format(i)) as f:
# 		js = f.read()
# 	repos = json.loads(js)



conn.close()