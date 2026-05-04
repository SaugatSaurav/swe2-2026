#!/bin/bash

touch head.txt
ts=$EPOCHREALTIME 
$ts > head.txt
read h n c < datei.txt
if test "$n" != "-"; then
echo $h



