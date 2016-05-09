# Running the Shell script

Make sure you have your Github access tokens in /GithubLanguage/analysis/access_token.secret

One token per line.

To run the script, type

`$ ./run.sh`

Check if you have permission to execute the script. `chmod 700 run.sh` if necessary.

The reporting files generated be the script can be found in /Shell/reports/

There will also be a copy of reporting files in /Shell/tmp/ , which will be deleted next time the script runs.


### Common Crawler usage example
```
$ ./run.sh --most-widely-used-web
Please specify the year (4 digits) followed by [ENTER]:
> 2015
Please specify the month (1 or 2 digits) followed by [ENTER]:
> 1
processing...
The most widely-used website in 012015 is: http://m.mlb.com
xyao01@ubuntu:~/Desktop/shell$ ./run.sh --tech-in-web
Please type the WEBSITE you are intereted in followed by [ENTER]:
> http://m.mlb.com
Please specify the year (4 digits) followed by [ENTER]:
> 2015
Please specify the month (1 or 2 digits) followed by [ENTER]:
> 1
processing...
These technologies are used in http://m.mlb.com: [jquery/jquery.min.js  jquery-plugins/jquery.history.js  jquery-writecapture/jquery.writecapture.js  jquery.bindable.min.js  jquery.writecapture.js  jquery-writecapture/writecapture.js  jquery-plugins/jquery.lazyload.js  jquery]
$ 
```
