#!/bin/bash

clear
if javac -cp .:../h2-1.4.191.jar:../interfaces:../server *.java
    then 
    java -cp .:../h2-1.4.191.jar:../interfaces:../server Chat
fi

