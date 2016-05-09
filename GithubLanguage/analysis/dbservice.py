import sqlite3

def get_cursor():
	conn = sqlite3.connect('../repos-sm.sqlite')
	c = conn.cursor()
	return c
