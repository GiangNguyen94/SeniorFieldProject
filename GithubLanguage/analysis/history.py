import re
import fork_history, commit_history, issue_event_history, search, score
import IPython

def find_by_name(name, exact=True, limit=10, report_type=["fork","commit","issue_event"]):
	if type(report_type) is list:
		if "fork" in report_type:
			fork_history.find_by_name(name, exact=exact, limit=limit)
		if "commit" in report_type:
			commit_history.find_by_name(name, exact=exact, limit=limit)
		if "issue_event" in report_type:
			issue_event_history.find_by_name(name, exact=exact, limit=limit)

def find_by_full_name(name, report_type=["fork","commit","issue_event"]):
	if type(report_type) is list:
		if "fork" in report_type:
			fork_history.find_by_full_name(name)
		if "commit" in report_type:
			commit_history.find_by_full_name(name)
		if "issue_event" in report_type:
			issue_event_history.find_by_full_name(name)

def find_by_full_names(full_name_list, report_type=["fork","commit","issue_event"]):
	if type(report_type) is list:
		if "fork" in report_type:
			fork_history.find_by_full_names(full_name_list)
		if "commit" in report_type:
			commit_history.find_by_full_names(full_name_list)
		if "issue_event" in report_type:
			issue_event_history.find_by_full_names(full_name_list)

def find_by_search_query(q, limit=10, sort="", order="desc", report_type=["fork","commit","issue_event"]):
	full_name_list = search.search(q, limit=limit, sort=sort, order=sort)
	find_by_full_names(full_name_list, report_type)

def find_live_by_search_query(q, limit=10, sort="", order="desc", report_type=["fork","commit","issue_event"]):
	results = search.search_raw(q, limit=limit, sort=sort, order=order)
	if "fork" in report_type:
		fork_params = [[item["owner"]["login"], item["name"], re.sub("{.*}", "", item["forks_url"])] for item in results["items"]]
		fork_history.find_raws(fork_params)
	if "commit" in report_type:
		commit_params = [[item["owner"]["login"], item["name"], re.sub("{.*}", "", item["commits_url"])] for item in results["items"]]
		commit_history.find_raws(commit_params)
	if "issue_event" in report_type:
		event_params = [[item["owner"]["login"], item["name"], re.sub("{.*}", "", item["issue_events_url"])] for item in results["items"]]
		issue_event_history.find_raws(event_params)
	# IPython.embed()

def find_live_by_search_query_with_selection(q, limit=25, sort="", order="desc", report_type=["fork","commit","issue_event"]):
	results = search.search_raw(q, limit=limit, sort=sort, order=order)
	if results["total_count"] == 0:
		print "No Repositories Matched Your Query"
		return False
	else:
		if results["total_count"] <= limit:
			print "{} Repositories Found".format(results["total_count"])
		else:
			print "{} repositories found, showing first {}".format(results["total_count"], limit)
		print
		print "{:<5} {:^20} {:^30} {:>9} {:>9} {:>9}".format("", "user","repo","score","stars","forks")
		show_list = map(lambda x:{"user":x["owner"]["login"],"repo":x["name"],"stars":x["stargazers_count"],"forks":x["forks_count"]}, results["items"])
		for idx, item in enumerate(show_list):
			print "{:<5} {:<20.20} {:<30.30} {:>9} {:>9} {:>9}".format("[{}]".format(idx), item["user"],item["repo"],score.pre_score(stars=item["stars"],forks=item["forks"]),item["stars"],item["forks"])
		print
		choice = __get_choice_from_input(limit)
		results = {"items":[results["items"][choice]]}
	if "fork" in report_type:
		fork_params = [[item["owner"]["login"], item["name"], re.sub("{.*}", "", item["forks_url"])] for item in results["items"]]
		fork_history.find_raws(fork_params)
	if "commit" in report_type:
		commit_params = [[item["owner"]["login"], item["name"], re.sub("{.*}", "", item["commits_url"])] for item in results["items"]]
		commit_history.find_raws(commit_params)
	if "issue_event" in report_type:
		event_params = [[item["owner"]["login"], item["name"], re.sub("{.*}", "", item["issue_events_url"])] for item in results["items"]]
		issue_event_history.find_raws(event_params)
	return True

def __get_choice_from_input(limit):
	while True:
		choice = raw_input("Choose one repo to continue: ")
		if choice.isdigit():
			choice = int(choice)
			if choice in range(0,limit):
				return choice
		print "Please enter 0 ~ {}".format(limit)

# find_live_by_search_query_with_selection("angular", limit=30, report_type=["commit","fork","issue_event"])