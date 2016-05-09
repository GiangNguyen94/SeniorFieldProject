import requests
import sys

username = sys.argv[1]
url = 'https://hacker-news.firebaseio.com/v0/user/'+username+'.json'
response=requests.get(url)
json_data=json_loads(response.text)
count = json_data['karma'] #returns int

if count<10:
	score=1.0
else:
	score=math.log10(count)

with open("tmp/user_hackers.score", "w") as out:
	out.write(str(score))