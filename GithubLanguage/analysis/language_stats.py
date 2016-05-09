import json
import os
import urllib
import OAuth
import IPython

def report_file():
	if not os.path.exists("reports"):
		os.makedirs("reports")
	return "reports/language_stats.csv"

def get_language_counts(start=0, end=3, file_end=100):
	report_dict={}
	with open(report_file(), "w") as output:
		for idir in range(start, end):
			dir_name = "../repos/part{}".format(idir)
			for ifile in range(0,file_end): 
				file_number = idir*100+ifile
				file_name = "repos_{}.json".format(file_number)
				with open(dir_name+"/"+file_name) as f:
					dic = json.loads(f.read())
					for d in dic:
						url = "{}?access_token={}".format(d["languages_url"],OAuth.token())
						langs = json.loads(urllib.urlopen(url).read())
						if u'message' in langs: # some repos may have been deleted since the repos were recorded
							continue
						for lang, count in langs.iteritems():
							if lang in report_dict:
								report_dict[lang]["lines"] += int(count)
								report_dict[lang]["repos"] += 1
							else:
								report_dict[lang]={}
								report_dict[lang]["lines"] = int(count)
								report_dict[lang]["repos"] = 1
		items = sorted(report_dict.items(), key=lambda x:x[1]["repos"], reverse=True)
		print >> output,"language,lines,repos"
		for item in items:
			print >> output, "{},{},{}".format(item[0],item[1]["lines"],item[1]["repos"])

# warning: may exceed rate limit
# ~(arg1-arg0)*arg3 api calls
get_language_counts(0,1,10) 