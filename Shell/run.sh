#!/bin/bash
# ./run.sh [--most-popular]
##### REPL
function repl {
	echo "this is repl"
}

##### most-popular
function most_popluar {
	echo "this is most popluar"
}

##### documentation
function usage {
	echo "this is usage"
}

##### main
if [ $# -gt 0 ]; then
	case $1 in
		--most-popular )	most_popluar
							;;
		-h | --help )		usage
							exit
							;;
		* )					usage
							exit 1
	esac
else
	repl
fi

