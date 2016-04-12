import fork_history
import commit_history
import issue_event_history
import search
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

def find_by_search_query(q, limit=10, sort="", order="", report_type=["fork","commit","issue_event"]):
	full_name_list = search.search(q, limit=limit, sort=sort, order=sort)
	find_by_full_names(full_name_list, report_type)

find_by_search_query("grit", limit=5)