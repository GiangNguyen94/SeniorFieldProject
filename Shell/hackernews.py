import requests
import sys
import json
import math


username = sys.argv[1]
url = "https://hacker-news.firebaseio.com/v0/user/"+username+".json"
response=requests.get(url)
json_data=json.loads(response.text)
count = 0
try:
	count=json_data['karma']
except:
	pass #returns int

score=1.0
if count<10:
	score=1.0
else:
	score=math.log10(count)

with open("tmp/user_hackers.score", "w") as out:
	out.write(str(score))