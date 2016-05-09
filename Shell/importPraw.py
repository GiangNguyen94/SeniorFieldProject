import praw
import pprint

username = input('Enter username: ')
user_agent = "Karma breakdown for" + username
r = praw.Reddit(user_agent=user_agent)
user = r.get_redditor(username)
gen=user.get_submitted()
karma_by_subreddit={}
highestscore=0
highest=''
for thing in gen:
	subreddit=thing.subreddit.display_name
	karma_by_subreddit[subreddit]=(karma_by_subreddit.get(subreddit,0)+thing.score)
	if thing.score>highestscore:
		highestscore=thing.score
		highest=thing.title

pprint.pprint(karma_by_subreddit)
print(thing.title) 