#!/bin/bash

set -o allexport

MYSQL_USERNAME='root'
MYSQL_PASSWORD='TigerBit!2016'

# HOSTS
MASTER=ec2-52-15-240-190.us-east-2.compute.amazonaws.com
SLAVE=(ec2-13-59-33-146.us-east-2.compute.amazonaws.com ec2-52-14-202-232.us-east-2.compute.amazonaws.com)
CANDIDATE=(ec2-13-58-247-217.us-east-2.compute.amazonaws.com)

set +o allexport

