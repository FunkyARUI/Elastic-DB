#this should be called from source
SCRIPT_HOME=/home/ubuntu/elasticDB/scripts

echo "SCRIPT_HOME is set to $SCRIPT_HOME"

MYSQL_HOME=$SCRIPT_HOME/../mysql

echo "MYSQL_HOME is set to $MYSQL_HOME"

source $SCRIPT_HOME/set_env.sh

SOURCE=$1
TARGET=$2
MASTER=$3

echo "$1 $2 $3"

echo "step 1 stop the source slave"
mysql --user="$MYSQL_USERNAME" --password="$MYSQL_PASSWORD" -e "stop slave;"

echo "step 2 get source slave's status"
var=$(cat /var/lib/mysql/relay-log.info | xargs | awk -F" " '{print $1,$2,$3,$4}')
set -- $var
echo "relay bin is $1, pos is $2, master bin is $3, pos is $4"

#this should be called in Ubuntu
sed -e "s/vader-1-vm3/$MASTER/ig" $SCRIPT_HOME/slave-template.sql > $SCRIPT_HOME/grantSlave.sql
sed -i "s/mysql-bin.000002/$3/ig" $SCRIPT_HOME/grantSlave.sql
sed -i "s/=445/=$4/ig" $SCRIPT_HOME/grantSlave.sql

scp $SCRIPT_HOME/grantSlave.sql root@$TARGET:$SCRIPT_HOME/grantSlave.sql

rm -rf $SCRIPT_HOME/grantSlave.sql*

echo "step 3 stop target $TARGET"
ssh root@$TARGET "/etc/init.d/mysql stop"

echo "step 4 copy files from $SOURCE to target $TARGET"
servernum=$[ ( $RANDOM % 900  ) + 100 ]
ssh root@$TARGET "$SCRIPT_HOME/initSlave.sh $SOURCE `expr $servernum`"

#sounds like it will automatically read it.
#echo "step 5 set the relay bin"
#ssh root@$TARGET "mysqlbinlog --start-position=$2 /var/lib/mysql/$1 | mysql --user="$MYSQL_USERNAME" --password="$MYSQL_PASSWORD""

echo "step 6 set the master bin"
ssh root@$TARGET "mysql --user="$MYSQL_USERNAME" --password="$MYSQL_PASSWORD" < $SCRIPT_HOME/grantSlave.sql"

echo "step 7 start the source slave"
mysql --user="$MYSQL_USERNAME" --password="$MYSQL_PASSWORD" -e "start slave;"

ssh root@$TARGET "rm -rf $SCRIPT_HOME/grantSlave.sql"

echo "finish"

