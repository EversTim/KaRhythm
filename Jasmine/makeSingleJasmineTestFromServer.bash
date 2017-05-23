#!/bin/bash

# If no arguments are passed, use the default
testNumber=0
if [ $# -eq 1 ]
	then
		testNumber=$1
fi

scratch=$(mktemp -d -t tmp.XXXXXXXXXX)

function cleanup {
	rm -rf $scratch	
}

trap cleanup EXIT

curl -I http://localhost:9000/test/$testNumber > $scratch/headers.html

if ! grep "HTTP/[0-9]\.[0-9] 200" $scratch/headers.html &> /dev/null
	then
		exit 1
fi

curl http://localhost:9000/test/$testNumber > $scratch/testFile.html

bodyTagLineNumber=$(grep -n -m 1 \<body\> $scratch/testFile.html | cut -f 1 -d ":")
firstScriptTagLineNumber=$(grep -n -m 1 \<script $scratch/testFile.html | cut -f 1 -d ":")
lineAfterBodyTagNumber=$((bodyTagLineNumber+1))
lineBeforeScriptTagNumber=$((firstScriptTagLineNumber-1))

echo "<div id=\"originalhtml\" style=\"display: none;\">" > $scratch/testFileBody.html

sed -n ""$lineAfterBodyTagNumber","$lineBeforeScriptTagNumber"p" $scratch/testFile.html >> $scratch/testFileBody.html

echo "</div>" >> $scratch/testFileBody.html

insertBefore=$(grep -n -m 1 "<!-- include source files here... -->" SpecRunnerTemplate.html | cut -f 1 -d ":")

sed -e ""$((insertBefore-1))"r$scratch/testFileBody.html" SpecRunnerTemplate.html > SpecRunner.${testNumber}.html
