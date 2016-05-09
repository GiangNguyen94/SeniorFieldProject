import history
import sys

print "running python script"
history.find_live_by_search_query_with_selection(sys.argv[1], limit=int(sys.argv[2]))
