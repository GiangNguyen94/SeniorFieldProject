import sqlite3, json, os, urllib, datetime, shutil, urllib2, re
import OAuth, dbservice
import progressbar
import IPython

def create_report_folder():
	folder_name = "reports/issue_events{}".format(datetime.datetime.now().strftime("%Y%m%d%H%M%S"))
	if not os.path.exists(folder_name):
		os.makedirs(folder_name)
	return folder_name

def get_from_url(url):
	results = {}
	url = "{}?per_page=100".format(url)
	page=1
	next_url = "{}&page={}".format(url,page)
	events_json = urllib.urlopen(next_url).read()
	events = json.loads(events_json)
	# for progress bar
	last_page = __get_last_page(next_url)
	if last_page>0:
		bar = __get_progressbar(last_page)
		print "Getting issue event history. About {} API calls will be made".format(last_page)
		bar.start()
	# read page by page	
	while type(events) is list and len(events)>0:		
		for event in events:
			results[event["id"]]=[event["id"], event["created_at"]]
		if last_page>0:
			bar.update(page)
		page += 1
		next_url = "{}&access_token={}&page={}".format(url,OAuth.token(),page)
		events_json = urllib.urlopen(next_url).read()
		events = json.loads(events_json)
	if last_page>0:
		bar.finish()
	return results

def report_events(hit, folder_name):
	subfolder_name = "{}/{}".format(folder_name,hit[0])
	if not os.path.exists(subfolder_name):
		os.makedirs(subfolder_name)
	records = get_from_url(hit[2]).values()
	sorted_records = sorted(records, key=lambda l:l[1]) #sort by event id
	filename = "{}/{}.csv".format(subfolder_name, hit[1])
	with open(filename,"w") as f:
		print >> f, "repo_name,time,id"
		for record in sorted_records:
			print >> f, "{},{},{}".format(hit[1],record[1],record[0])
	print "Find issue event history in {}".format(filename)
	if not os.path.exists("tmp/issue_event/"):
		os.makedirs("tmp/issue_event/")
	shutil.copy(filename, "tmp/issue_event")


def find_raw(username, reponame, url, folder=None):
	if folder is None:
		folder = create_report_folder()
	report_events([username, reponame, url], folder)

def find_raws(params):
	folder = create_report_folder()
	for param in params:
		report_events(param, folder)

def find_by_name(name, exact=True, limit=10):
	c = dbservice.get_cursor()
	if exact:
		c.execute("SELECT owner, name, issue_events_url FROM repos WHERE name LIKE ?", (name,))
	else:
		c.execute("SELECT owner, name, issue_events_url FROM repos WHERE name LIKE ?", ("%{}%".format(name),))
	report_folder = create_report_folder()
	for hit in c.fetchall()[:limit]:
		report_events(hit, report_folder)
	print "Completed. Find reports in {}".format(report_folder)
	c.close()

def find_by_full_name(full_name):
	c = dbservice.get_cursor()
	c.execute("SELECT owner, name, issue_events_url FROM repos WHERE full_name LIKE ?", (full_name,))
	report_folder = create_report_folder()
	for hit in c.fetchall():
		report_events(hit, report_folder)
	print "Completed. Find reports in {}".format(report_folder)
	c.close()

def find_by_full_names(full_name_list):
	c = dbservice.get_cursor()
	report_folder = create_report_folder()
	for name in full_name_list:
		c.execute("SELECT owner, name, issue_events_url FROM repos WHERE full_name LIKE ?", (name,))
		for hit in c.fetchall():
			report_events(hit, report_folder)
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
