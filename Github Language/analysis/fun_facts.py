import user_stats
import history
import repo_user
# stargazers

# language q=language:xxx

# users with most repos(easy)
# user_stats.get_user_repo_counts(0,3)

# 5 repos with most stars #include history
history.find_live_by_search_query("stars:>=100", sort="stars", limit=15)

# owners and contributers of the 5 repos with most stars
# repo_user.find_live_by_search_query("stars:>=100", sort="stars", limit=5)

# find contributors of a repo, repositories/1/contributors

# the top 30 java repos with the most stars (search/repositories?q=language:Java&sort=stars)
# history.find_live_by_search_query("language:Java", sort="stars", limit=30)