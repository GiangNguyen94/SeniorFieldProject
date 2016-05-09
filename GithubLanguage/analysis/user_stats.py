import json
import os

def report_file():
	if not os.path.exists("reports"):
		os.makedirs("reports")
	return "reports/user_stats.csv"

def get_user_repo_counts(start=0, end=49):
	user_dict={}
	with open(report_file(), "w") as output:
		for idir in range(start, end):
			dir_name = "../repos/part{}".format(idir)
			for ifile in range(0,100):
				file_number = idir*100+ifile
				file_name = "repos_{}.json".format(file_number)
				with open(dir_name+"/"+file_name) as f:
					dic = json.loads(f.read())
					for d in dic:
						username = d["owner"]["login"]
						if username in user_dict:
							user_dict[username] += 1
						else:
							user_dict[username] = 1
		items = sorted(user_dict.items(), key=lambda x:x[1], reverse=True)
		print >> output,"Username,Repos"
		for item in items:
			print >> output, "{},{}".format(item[0],item[1])
