import fork_history
import commit_history
import issue_event_history
import search
import IPython
import re

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
	IPython.embed()