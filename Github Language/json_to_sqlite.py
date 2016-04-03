import sqlite3
import json
import re

conn = sqlite3.connect('repos-sm.sqlite')
c = conn.cursor()



sql_key_list = ["name", "full_name", "owner", "languages_url", "issue_events_url", "forks_url", "commits_url"]
attrs = ",".join(map(lambda s: s+" text",sql_key_list))
c.execute("DROP TABLE IF EXISTS repos")
c.execute("CREATE TABLE repos ({})".format(attrs)) 


## repos_0, create table
def insert_data(start=0, end=49):
	for idir in range(start, end):
		dir_name="repos/part{}".format(idir)
		for ifile in range(0,100):
			file_number = idir*100+ifile
			file_name = "repos_{}.json".format(file_number)
			with open(dir_name+"/"+file_name) as f:
				repos = json.loads(f.read())
				for repo in repos:
					c.execute("INSERT INTO repos VALUES(?,?,?,?,?,?,?)", \
						(repo["name"], repo["full_name"], \
							repo["owner"]["login"], \
							repo["languages_url"], \
							re.sub("{.*}", "", repo["issue_events_url"]), \
							repo["forks_url"], re.sub("{.*}", "", repo["commits_url"])))



insert_data(0,3)
conn.commit()
conn.close()