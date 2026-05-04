#!/bin/bash
zeit=${EPOCHREALTIME/[.,]}
if test -e "head"; then
	read head < head
	current=$head
	read -r ts next content < $current
	while test "$next" != "-"; do 
		current=$next
		read -r ts next content < $current
	done
	echo "$ts $zeit $content" > $current
	echo "$zeit - $1" > $zeit
else
	echo $zeit > head
fi 
	
