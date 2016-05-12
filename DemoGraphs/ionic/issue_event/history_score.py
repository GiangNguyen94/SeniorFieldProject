import csv

with open('RTable.csv', 'r') as f:
	reader = csv.reader(f)
	score_list=list(reader)

score_list.reverse()
score_list = list(map(lambda x:float(x[0]), score_list))

final_score=0.0
percentage=1.0
for score in score_list:
	percentage *= 0.9
	discounted_score = score * percentage / 0.9
	final_score += discounted_score

with open('history.score', 'w') as f:
	f.write(str(final_score))