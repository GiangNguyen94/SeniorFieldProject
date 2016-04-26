#!/bin/bash
# ./run.sh [--most-popular]
##### REPL

function repl {
	echo "this is repl"
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
	target_file=(~/Desktop/shell/commoncrawl_data/data1-web_ip_country/$filename/website.csv)

	#read data from target directory
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
}

##### most widely-used technology in particular month of a year
function most_widely_used_tech {
	echo "most widely-used technology in particular month of a year"
}

##### technologies used in website
function web_tech {
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
	target_file=(~/Desktop/shell/commoncrawl_data/data4-web_components/$filename/webComponents.csv)

	#read from target file
	technologies=""
	while read line; do
		IFS=',' read -ra array <<< "$line"
		#printf '%s\n' "${array[@]}"
		length=${#array[@]}
		if [[ "${array[0]}" == "$website" ]]; then
			echo "These technologies are used in $website: ${array[@]:1:$length-1}"
			break
		fi
	done < "$target_file"

}

##### website server delopyment
function web_depolyment {
	echo "website depolyment"
}

##### documentation
function usage {
	echo "this is usage"
}

##### main
if [ $# -gt 0 ]; then
	case $1 in
		--most-widely-used-web | -mwuw )	most_widely_used_web
							              														;;
		--most-widely-used-tech | -mwut )	most_widely_used_tech
							              														;;
		--web-depolyment | -wd ) web_depolyment
																				;;
		--tech-in-web | -tiw) web_tech
																;;
		-h | --help )		           usage
							            			exit
							              			;;
		* )					           		usage
							          			exit 1
	esac
else
	repl
fi
