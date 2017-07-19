#!/bin/bash

HOME=/home/ubuntu/elasticDB

echo "HOME is set to $HOME"

SCRIPT_HOME=/home/ubuntu/elasticDB/scripts

echo "SCRIPT_HOME is set to $SCRIPT_HOME"

CURRENT_HOME="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

echo "CURRENT_HOME is set to $CURRENT_HOME"

source $CURRENT_HOME/set_env.sh

TPCW_HOME=$CURRENT_HOME/../tpcw

TPCW_HOME="$( cd "$TPCW_HOME" && pwd )"

echo "TPCW_HOME is set to $TPCW_HOME"

# Check scp from local to all servers
echo "*** Check scp from local to all servers *********************************"
for i in $MASTER ${SLAVE[@]} ${CANDIDATE[@]}
do
echo "check local to $i"
ssh -o StrictHostKeyChecking=no -o BatchMode=yes root@$i "hostname"
done

echo "*** sync elastic db *********************************"
for i in $MASTER ${SLAVE[@]} ${CANDIDATE[@]}
do
echo "sync to $i"
ssh root@$i "rm -rf $HOME"
ssh root@$i "mkdir -p $HOME"
scp -r $CURRENT_HOME root@$i:$HOME
scp -r $TPCW_HOME root@$i:$HOME
done


# Check scp to all servers
echo "*** checking scp to all servers *********************************"

for i in $MASTER ${SLAVE[@]} ${CANDIDATE[@]}
do
for j in $MASTER ${SLAVE[@]} ${CANDIDATE[@]} 
do
echo "$i at $j"
ssh -o StrictHostKeyChecking=no -o BatchMode=yes root@$i "ssh root@$j "hostname""
done
done

# Check scp to all servers
echo "*** change my.cnf to open to everywhere *********************************"
for i in $MASTER ${SLAVE[@]} ${CANDIDATE[@]}
do
echo "configure mysql at $i"
ssh root@$i "sed -i "s/127.0.0.1/0.0.0.0/ig" /etc/mysql/my.cnf"
ssh root@$i "/etc/init.d/mysql restart"
done





