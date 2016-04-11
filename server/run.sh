#!/bin/bash

clear
if javac -cp .:../h2-1.4.191.jar:../interfaces *.java
    then
    if (( $# > 1 )) || (( $# < 1 ))
        then 
            echo "usage: ./run.sh <ip of host>"
        else
            java -cp .:../h2-1.4.191.jar:../interfaces Server $1
        fi
fi

