import requests

username = input('enter username: ')
url = 'https://hacker-news.firebaseio.com/v0/user/'+username+'.json'
response=requests.get(url)
json_data=json_loads(response.text)
print(json_data['karma']) #returns int