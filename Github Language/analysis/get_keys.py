import json
import os

def report_file():
	if not os.path.exists("reports"):
		os.makedirs("reports")
	return "reports/keys_list.txt"

def get_keys(start=0, end=49):
	key_set = []
	with open(report_file(), "w") as output:
		for idir in range(start, end):
			dir_name = "../repos/part{}".format(idir)
			for ifile in range(0,100):
				file_number = idir*100+ifile
				file_name = "repos_{}.json".format(file_number)
				with open(dir_name+"/"+file_name) as f:
					dicts = json.loads(f.read())
					for d in dicts:
						for key in d.keys():
							if key not in key_set:
								key_set.append(key)
								print >> output, "{} {}".format(ifile, key)

get_keys()