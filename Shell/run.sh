#!/bin/bash
# ./run.sh [--most-popular]

##### Github
function repl {
	
	while true ; do
		
		echo -n "What are you interested in? Type q to exit > "
		read keyword
		cleanup
		if [ $keyword = q ]; then
			exit
		fi
		re='^[0-9]+$'
		
		echo -n "How many results do you want to see? "
		read limit			
		if ! [[ $limit =~ $re ]] ; then
		   limit = 25
		fi
		github $keyword $limit
		finalscore
		echo "Save the reports and graphs in tmp folder if you want. They will be deleted in the next search."
	done
}


function github () {
	echo "Searching $1 in GitHub"
	SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/../GithubLanguage/analysis" && pwd )"
	python $SCRIPT_DIR/run.py $1 $2
	cp RViz.R tmp/commit/
	cp RViz.R tmp/fork/
	cp RViz.R tmp/issue_event/
	cd tmp/commit/
	for i in `ls -a *.csv`
	do
		tmpname=$i
	done
	Rscript RViz.R $tmpname commit
	cd ..
	cd fork/
	for i in `ls -a *.csv`
	do
		tmpname=$i
	done
	Rscript RViz.R $tmpname fork
	cd ..
	cd issue_event/
	for i in `ls -a *.csv`
	do
		tmpname=$i
	done
	Rscript RViz.R $tmpname issue
	cd ..   #AT SHELL
	cd ..	#AT SENIOR FIELD PROJECT


	echo "Find the trend graphs in Shell/tmp/[commit|fork|issue_event]"
	echo "All files in tmp/ will be overwritten at next run, copy desired files to secure location."

}

function finalscore {

	read name < tmp/username.txt
	touch tmp/user_reddit.score
	touch tmp/user_hackers.score
	python3 importPraw.py $name
	read USER_SCORE < tmp/user_reddit.score
	python3 hackernews.py $name
	read HACKERS_SCORE < tmp/user_hackers.score

	cp history_score.py tmp/commit/
	cp history_score.py tmp/fork/
	cp history_score.py tmp/issue_event/
	cd tmp/commit/
	python3 history_score.py
	read COMMIT_SCORE < history.score
	
	cd ..
	cd fork/
	python3 history_score.py
	read FORK_SCORE < history.score

	cd ..
	cd issue_event/
	python3 history_score.py
	read ISSUE_SCORE < history.score

	cd ..   #AT SHELL
	cd ..	#AT SENIOR FIELD PROJECT

	FINAL_SCORE=`echo "($FORK_SCORE + $COMMIT_SCORE + $ISSUE_SCORE) * ($USER_SCORE + $HACKERS_SCORE) " | bc`
	echo "The Final Score is " $FINAL_SCORE
}


function cleanup {
	rm -rf tmp
}


##### most-popular
function most_widely_used_web {
	#Let user input
	echo "Please specify the year (4 digits) followed by [ENTER]:"
	read -p '> ' year
	while [[ $year -ne "2014"  && $year -ne "2015" ]];
	do
		echo "Please re-type the year (2014 or 2015) followed by [ENTER]:"
		read -p '> ' year
	done
	echo "Please specify the month (1 or 2 digits) followed by [ENTER]:"
	read -p '> ' month
	while [[ $month -lt 1 || $month -gt 12 ]];
	do
		echo "Please re-type the month (1 to 12) followed by [ENTER]:"
		read -p '> ' month
	done
	if [[ $month -le 9 && $month -ge 1 ]]; then
		month="0$month"
	fi

	echo "processing..."

	#build target directory
 filename=""
  
	filename="$month$year"
	#target_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )/../CommonCrawlData/data1-web-ip-country/$filename/" && pwd )"
	target_file=(~/Desktop/untitled\ folder/data1-web\ ip\ country/$filename/website.csv)
	#echo "$target_file"
	

	#read data from target directory
	#echo $cond
	
		popular_web=""
		max_number=0
		while read line; do
			while IFS=',' read -ra array; do
				if [[ ${array[1]} -gt $max_number ]]; then
					popular_web=${array[0]}
					max_number=${array[1]}
				fi
			done <<< "$line"
		done < "$target_file"
		echo "The most widely-used website in $filename is: $popular_web"
		cd ..
		cd CommonCrawlData/websites-graph
		open $month$year.pdf
		cd ..
		cd ..
		cd Shell
	
}

##### most widely-used technology in particular month of a year
function most_widely_used_tech {
	echo "Please specify the year (4 digits) followed by [ENTER]:"
	read -p '> ' year
	while [[ $year -ne "2014"  && $year -ne "2015" ]];
	do
		echo "Please re-type the year (2014 or 2015) followed by [ENTER]:"
		read -p '> ' year
	done
	echo "Please specify the month (1 or 2 digits) followed by [ENTER]:"
	read -p '> ' month
	while [[ $month -lt 1 || $month -gt 12 ]];
	do
		echo "Please re-type the month (1 to 12) followed by [ENTER]:"
		read -p '> ' month
	done
	if [[ $month -le 9 && $month -ge 1 ]]; then
		month="0$month"
	fi
	filename=""
	filename="$month$year.pdf"
	target_file=(../$filename)
		cd ..
		cd CommonCrawlData/technologies-graph
		open $month$year.pdf
		
		cd .. #commoncrawldata
		cd .. #seniorfieldproject
		cd Shell

}

##### technologies used in website and website depolyment
function web_information {
	#let user input
	echo "Please type the WEBSITE you are intereted in followed by [ENTER]:"
	read -p '> ' website
	echo "Please specify the year (4 digits) followed by [ENTER]:"
	read -p '> ' year
	while [[ $year -ne "2014"  && $year -ne "2015" ]];
	do
		echo "Please re-type the year (2014 or 2015) followed by [ENTER]:"
		read -p '> ' year
	done
	echo "Please specify the month (1 or 2 digits) followed by [ENTER]:"
	read -p '> ' month
	while [[ $month -lt 1 || $month -gt 12 ]];
	do
		echo "Please re-type the month (1 to 12) followed by [ENTER]:"
		read -p '> ' month
	done
	if [[ $month -le 9 && $month -ge 1 ]]; then
		month="0$month"
	fi

	echo "processing..."

	#build target directory
	filename=""
	filename="$month$year"
	target_file=(~/Desktop/SeniorFieldProject/CommonCrawlData/data4-web-components/$filename/webComponents.csv)
	target_file_deploy=(~/Desktop/SeniorFieldProject/CommonCrawlData/data3-web-deployments/$filename/websiteDeployments.csv)

	#read from the target file in web components
	while read line; do
		IFS=',' read -ra array <<< "$line"
		#printf '%s\n' "${array[@]}"
		length=${#array[@]}
		if [[ "${array[0]}" == "$website" ]]; then
			echo "These technologies are used in $website: ${array[@]:1:$length-1}"
			break
		fi
	done < "$target_file"

	#read from the web deployment file
	line_count=0
	country_name_array=()
	num_array=()
	content_array=()
	while read line; do
		if [[ $line_count -eq 0 ]]; then
			let "line_count=line_count+1"
			IFS=',' read -ra country_name_array <<< "$line"
		else
			IFS=',' read -ra num_array <<< "$line"
			length=${#num_array[@]}
			if [[ "${num_array[0]}" == "$website" ]]; then
				index=0
				for var in "${num_array[@]:1:$length-1}"
				do
					let "index=index+1"
					if [[ ${var} -gt 0 ]]; then
						content_array+=( "${country_name_array[$index]}" )
					fi
				done
				break
			fi
		fi
	done < "$target_file_deploy"
	echo "The server of $website is deployed at: ${content_array[@]}"
}

##### documentation
function usage {
	echo "                                  GitHub"
	echo "--most-widely-used-web|-mwuw      most_widely_used_web"
	echo "--most-widely-used-tech|-mwut     most_widely_used_tech"
	echo "--web-information|-wi             web_information"
	echo "--help|-h                         help"
}

##### main
if [ $# -gt 0 ]; then
	case $1 in
		--most-widely-used-web | -mwuw )	most_widely_used_web
							              	;;
		--most-widely-used-tech | -mwut )	most_widely_used_tech
							              	;;
		--web-information | -wi) 			web_information
											;;
		-h | --help )		           		usage
											exit
											;;
		* )					           		usage
							          		exit 1
	esac
else
	repl
fi
