#!/bin/bash

exitstatus=0
testPage=0

while [[ $exitStatus -eq 0 ]] ; do
	./makeSingleJasmineTestFromServer.bash $testPage
	exitStatus=$?
	((testPage++))
done
