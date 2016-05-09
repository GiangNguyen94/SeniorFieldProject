require 'json'

# users_json = %x[curl "https://api.github.com/users"]
# users = JSON.parse(users_json)

repos_json = %x[curl "https://api.github.com/repositories"]
repos = JSON.parse(repos_json)

counter = 0
lang_hash = {}
repos.each do |repo|
	puts repo
	lang_url = repo["languages_url"]
	break if counter>=10
	lang_json = %x[curl #{lang_url}]
	counter += 1
	langs = JSON.parse(lang_json)
	langs.each do |lang|
		if lang_hash.has_key?(lang)
			lang_hash[lang] += 1
		else
			lang_hash[lang] = 1
		end
	end
end

f = open("language_stats.txt","w")
f.puts(lang_hash.to_json)