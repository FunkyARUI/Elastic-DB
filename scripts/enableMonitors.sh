#!/bin/bash

HOME=/home/ubuntu/elasticDB

echo "HOME is set to $HOME"

SCRIPT_HOME=/home/ubuntu/elasticDB/scripts

echo "SCRIPT_HOME is set to $SCRIPT_HOME"

TPCW_HOME=/home/ubuntu/elasticDB/tpcw

echo "TPCW_HOME is set to $TPCW_HOME"

LOCAL_SCRIPT_HOME="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

echo "LOCAL_SCRIPT_HOME is set to $LOCAL_SCRIPT_HOME"

source $LOCAL_SCRIPT_HOME/set_env.sh

# enable CanvasJS on master
echo "*** enable CanvasJS on master *********************************"
ssh root@$MASTER "rm -rf /var/lib/tomcat7/webapps/WebContent"
ssh root@$MASTER "tar xzvf $TPCW_HOME/WebContent.tar.gz -C /var/lib/tomcat7/webapps" 

# unzip dstats on all servers
echo "*** unzip dstats on all servers *********************************"
for i in $MASTER ${SLAVE[@]} ${CANDIDATE[@]}
do
echo "enable dstats on $i"
ssh root@$i "tar xzvf $TPCW_HOME/dstats.tar.gz -C $TPCW_HOME"
#ssh root@$i "$TPCW_HOME/./dstat-0.7.3/dstat"
osascript -e 'tell application "Terminal" to do script "echo '$i' && ssh root@'$i' \"'$TPCW_HOME'/./dstat-0.7.3/dstat --color 5 \""'
done



