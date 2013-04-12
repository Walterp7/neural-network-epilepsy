#!/bin/bash
for p in ./noise* 
do
java   -Xms256m  -Xmx60024m -jar Simulator1T-time-test.jar 391781649 `basename $p` 4 column_config.txt synapse_config.txt time
java   -Xms256m  -Xmx60024m -jar Simulator1T-time-test.jar 2348721690 `basename $p` 4 column_config.txt synapse_config.txt time
java   -Xms256m  -Xmx60024m -jar Simulator1T-time-test.jar 6783257431 `basename $p` 4 column_config.txt synapse_config.txt time

done