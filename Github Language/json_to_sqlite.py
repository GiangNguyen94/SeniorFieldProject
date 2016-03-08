import sqlite3
import json

conn = sqlite3.connect('repos.sqlite')
c = conn.cursor()

with open("repos/repos_0.json") as f:
	js = f.read()

# Possible SQL injection
repos = json.loads(js)
attrs = ",".join(map(lambda s: s+" text",repos[0].keys()))
c.execute("DROP TABLE IF EXISTS repos")
c.execute("CREATE TABLE repos ({})".format(attrs))
for repo in repos:
	for key in repo.keys():
		c.execute("INSERT INTO repos({}) VALUES(?)".format(key), (unicode(repo[key]),))

conn.commit()
conn.close()