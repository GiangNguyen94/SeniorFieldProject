import sqlite3, json, os, urllib, datetime, shutil, urllib2, re
import OAuth, dbservice
import progressbar
import IPython

def create_report_folder():
	folder_name = "reports/forks{}".format(datetime.datetime.now().strftime("%Y%m%d%H%M%S"))
	if not os.path.exists(folder_name):
		os.makedirs(folder_name)
	return folder_name

def get_from_url(url):
	results = {}
	url = "{}?per_page=100".format(url)
	page=1
	next_url = "{}&page={}".format(url,page)
	forks_json = urllib.urlopen(next_url).read()
	forks = json.loads(forks_json)
	# for progress bar
	last_page = __get_last_page(next_url)
	if last_page > 0:
		bar = __get_progressbar(last_page)
		print "Getting issue event history. About {} API calls will be made".format(last_page)
		bar.start()
	# read page by page	
	while type(forks) is list and len(forks)>0:		
		for fork in forks:
			results[fork["id"]]=[fork["id"], fork["created_at"]]
		if last_page > 0:
			bar.update(page)
		page += 1
		next_url = "{}&access_token={}&page={}".format(url,OAuth.token(),page)
		forks_json = urllib.urlopen(next_url).read()
		forks = json.loads(forks_json)
	if last_page > 0:
		bar.finish()
	return results

def report_forks(hit, folder_name):
	subfolder_name = "{}/{}".format(folder_name,hit[0])
	if not os.path.exists(subfolder_name):
		os.makedirs(subfolder_name)
	records = get_from_url(hit[2]).values()
	sorted_records = sorted(records, key=lambda l:l[1]) #sort by fork id
	filename = "{}/{}.csv".format(subfolder_name, hit[1])
	with open(filename,"w") as f:
		print >> f, "repo_name,time,id"
		for record in sorted_records:
			print >> f, "{},{},{}".format(hit[1],record[1],record[0])
	print "Find fork history in {}".format(filename)
	if not os.path.exists("tmp/fork/"):
		os.makedirs("tmp/fork")
	shutil.copy(filename,"tmp/fork")

def find_raw(username, reponame, url, folder=None):
	if folder is None:
		folder = create_report_folder()
	report_forks([username, reponame, url], folder)

def find_raws(params):
	folder = create_report_folder()
	for param in params:
		report_forks(param, folder)

def find_by_name(name, exact=True, limit=10):
	c = dbservice.get_cursor()
	if exact:
		c.execute("SELECT owner, name, forks_url FROM repos WHERE name LIKE ?", (name,))
	else:
		c.execute("SELECT owner, name, forks_url FROM repos WHERE name LIKE ?", ("%{}%".format(name),))
	report_folder = create_report_folder()
	for hit in c.fetchall()[:limit]:
		report_forks(hit, report_folder)
	print "Completed. Find reports in {}".format(report_folder)
	c.close()

def find_by_full_name(full_name):
	c = dbservice.get_cursor()
	c.execute("SELECT owner, name, forks_url FROM repos WHERE full_name LIKE ?", (full_name,))
	report_folder = create_report_folder()
	for hit in c.fetchall():
		report_forks(hit, report_folder)
	print "Completed. Find reports in {}".format(report_folder)
	c.close()

def find_by_full_names(full_name_list):
	c = dbservice.get_cursor()
	report_folder = create_report_folder()
	for name in full_name_list:
		c.execute("SELECT owner, name, forks_url FROM repos WHERE full_name LIKE ?", (name,))
		hits = c.fetchall()
		IPython.embed()
		for hit in hits:
			report_forks(hit, report_folder)
	print "Completed. Find reports in {}".format(report_folder)
	c.close()

def __get_last_page(url):
	response = urllib2.urlopen(url)
	header = response.info()
	if 'link' in header.dict:
		link = header.dict['link'].split(",")[1].split(";")[0].strip()
		last_page = re.split("(\?|\&)page\=", link)[-1].strip(">")
		return int(last_page)
	else:
		return -1

def __get_progressbar(maxval):
	return progressbar.ProgressBar(maxval=maxval, \
		widgets=[progressbar.Bar('=', '[', ']'), ' ', progressbar.Percentage()])

# Usage examples:
# find_by_name('grit', exact=True, limit=3)
# find_by_name('grit', exact=False, limit=30)
# find_by_full_name('mojombo/grit')
# find_by_full_names(['darrenboyd/grit', 'foca/integrity-email', 'foca/integrity'])