import history
import sys

history.find_live_by_search_query(sys.argv[1], sort="stars", limit=1, report_type=["issue_event"])