# SeniorFieldProject: GC Team
#### A Glance at Trending Technologies - A Senior Field Project by Xi, Xin, Giang
Brandeis, Spring 2016

## Objectives
Modern websites, mobile applications and open source projects are composed of many technologies and services from a variety of vendor. The relative popularity of these components is of interest to both developers and investors. The goal of this project is to research and test several discovery tools. 

## Methodology
We first came up with a Technology Whitelist that lists out the interesting technologies and their “signatures”. A technology signature can be anything from HTML tags to IP addresses linked to a known service. 

Regarding data sources, we decided to dig deep into three realms: CommonCrawl <<http://commoncrawl.org/>>, GitHub <<https://github.com/>>, Reddit <<https://www.reddit.com/>> and HackerNews <<https://news.ycombinator.com/>>. Refer to our linked paper report for the detailed work on each data source. For our demonstration, we narrowed down our search to 5 interesting technologies: Jquery, AngularJs, Ionic, React and Hubspot services. 

## Results
Our final result consists of several graphings demonstrating the use of websites and technologies from CommonCrawl data and allows user to search for repo of interest on GitHub (BONUS: the repo data will also return with a hotness score)

### Common Crawl
CommonCrawl data provides us with the usage trend of those aforementioned technolgies as well as the top six most active websites in the 18 months of data available. 

#### Technology Usage Trend
[JQUERY USAGE TREND](https://github.com/GiangNguyen94/SeniorFieldProject/blob/master/DemoGraphs/Jquery%20Trend.pdf)

#### Technology Usage By Month
[Dec 2015](https://github.com/GiangNguyen94/SeniorFieldProject/blob/master/CommonCrawlData/technologies-graph/012015.pdf)

User needs to specify month and year to see the statistics for that month. More can be found in [this folder](https://github.com/GiangNguyen94/SeniorFieldProject/tree/master/CommonCrawlData/technologies-graph)

#### Website Crawled Visits By Month
[Dec 2015](https://github.com/GiangNguyen94/SeniorFieldProject/blob/master/CommonCrawlData/websites-graph/012015.pdf)

User needs to specify month and year to see the statistics for that month.
More can be found in [this folder](https://github.com/GiangNguyen94/SeniorFieldProject/tree/master/CommonCrawlData/websites-graph)

### GitHub
Here we show some sample data from GitHub research

#### Trending: jQuery as an example
* Commit trend of [jQuery](https://github.com/GiangNguyen94/SeniorFieldProject/blob/master/DemoGraphs/commit_trend_for_repo_jquery.pdf)
* Fork trend of [jQuery](https://github.com/GiangNguyen94/SeniorFieldProject/blob/master/DemoGraphs/fork_trend_for_repo_jquery.pdf)
* Issue Event trend of [jQuery](https://github.com/GiangNguyen94/SeniorFieldProject/blob/master/DemoGraphs/issue_trend_for_repo_jquery.pdf)

#### Scoring: grit as an example
[grit](https://github.com/mojombo/grit) is one of the earliest repo on github.
It's final score is: 912.166

Here are the break down:
* Owner's username: [mojombo](https://github.com/GiangNguyen94/SeniorFieldProject/blob/master/DemoGraphs/grit-show-scoring/username.txt)
* Owner's Reddit score: [1.0](https://github.com/GiangNguyen94/SeniorFieldProject/blob/master/DemoGraphs/grit-show-scoring/user_reddit.score)
* Owner's HackerNews score: [3.69](https://github.com/GiangNguyen94/SeniorFieldProject/blob/master/DemoGraphs/grit-show-scoring/user_hackers.score)
* Repo's commit history score: [48.42](https://github.com/GiangNguyen94/SeniorFieldProject/blob/master/DemoGraphs/grit-show-scoring/commit/history.score)
* Repo's fork history score: [21.049](https://github.com/GiangNguyen94/SeniorFieldProject/blob/master/DemoGraphs/grit-show-scoring/fork/history.score)
* Repo's issue event history score: [124.98](https://github.com/GiangNguyen94/SeniorFieldProject/blob/master/DemoGraphs/grit-show-scoring/issue_event/history.score)

Also for your reference are:
* Repo's commit history: [this csv](https://github.com/GiangNguyen94/SeniorFieldProject/blob/master/DemoGraphs/grit-show-scoring/commit/grit.csv)
* Repo's fork history: [this.csv](https://github.com/GiangNguyen94/SeniorFieldProject/blob/master/DemoGraphs/grit-show-scoring/fork/grit.csv)
* Repo's issue event history: [this.csv](https://github.com/GiangNguyen94/SeniorFieldProject/blob/master/DemoGraphs/grit-show-scoring/issue_event/grit.csv)



## Code



## Architecture & Design
![Flow Chart](https://github.com/GiangNguyen94/raw/master/Report/flowchart.png)

## Scoring a Github repo


## Installation
* Github API access token
    - Follow the link to aquire your Github API access tokens: <https://help.github.com/articles/creating-an-access-token-for-command-line-use/>
    - Store your access token in `GithubLanguage/analysis/access_token.secret`, one token per line
    - Also copy to `GithubLanguage/access_token.secret` if you wish to run scripts outside of `analysis`
* Bash
* Python 2.7
    * urllib2
    * progressbar
    * ipython
* Python 3.4
    - Use `python3 -m pip install <package>` to specify the python version
    * praw
* R 3.2.4
    * ggplot2
* PDF Viewer
* Internet Access

## Suggestions for Future Work


## Team Members
* [Giang Nguyen](https://github.com/GiangNguyen94) <<giangcoi@brandeis.edu>>
    - Giang is a graduating Senior at Brandeis University.
* [Xi Qian](https://github.com/qxx) <<qxx@brandeis.edu>>
    - Xi is a graduating Master's student at Brandeis Univeristy.
* [Xin Yao](https://github.com/XinYao1992) <<xyao01@brandeis.edu>>
    - Xin is a Master's student at Brandeis University.