SCRIPT_HOME=/home/ubuntu/elasticDB/scripts

echo "SCRIPT_HOME is set to $SCRIPT_HOME"

CURRENT_HOME="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

echo "CURRENT_HOME is set to $CURRENT_HOME"

source $CURRENT_HOME/set_env.sh

echo "step 0 clean master"
for target in $MASTER
do
echo "clean DB in $MASTER"
ssh root@$MASTER "/etc/init.d/mysql start"
ssh root@$target "$SCRIPT_HOME/cleanDB.sh"
done

echo "step 1 stop master/slave process"
ssh root@$MASTER "/etc/init.d/mysql stop"

for slave in ${SLAVE[@]} ${CANDIDATE[@]}
do
echo "step 1 stop slave $slave"
ssh root@$slave "/etc/init.d/mysql stop"
done

echo "step 2 initialize master"
for target in $MASTER
do
echo "init $MASTER"
ssh root@$target "$SCRIPT_HOME/initMaster.sh 1"
ssh root@$target "mysql --user="$MYSQL_USERNAME" --password="$MYSQL_PASSWORD" < $SCRIPT_HOME/grantMaster.sql"
done

echo "step 3 get master status"
string=$(ssh root@$MASTER "echo "show master status" | mysql --user="$MYSQL_USERNAME" --password="$MYSQL_PASSWORD"")
var=$(echo $string | awk -F" " '{print $1,$2,$3,$4,$5,$6}')
set -- $var
echo "bin is $5, pos is $6"
# sed -i has problem in mac, we need to use -e if we would like to be compatible with linux
sed -e "s/vader-1-vm3/$MASTER/g" $CURRENT_HOME/slave-template.sql > $CURRENT_HOME/grantSlave.sql
sed -e "s/mysql-bin.000002/$5/g" $CURRENT_HOME/grantSlave.sql > $CURRENT_HOME/grantSlave.sql-bin
sed -e "s/=445/=$6/g" $CURRENT_HOME/grantSlave.sql-bin > $CURRENT_HOME/grantSlave.sql

echo "step 4 restart slaves"
for (( i = 0 ; i < ${#SLAVE[@]} ; i++ ))
do
target=${SLAVE[$i]}
echo "restart $target"
scp $CURRENT_HOME/grantSlave.sql root@$target:$SCRIPT_HOME/grantSlave.sql
servernum=$(echo $i+2 | bc)
ssh root@$target "$SCRIPT_HOME/initSlave.sh $MASTER `expr $servernum`"
ssh root@$target "mysql --user="$MYSQL_USERNAME" --password="$MYSQL_PASSWORD" < $SCRIPT_HOME/grantSlave.sql"
ssh root@$target "rm -rf $SCRIPT_HOME/grantSlave.sql"
echo "$target restarts"
done

echo "step 5 restart candidates"
for (( i = 0 ; i < ${#CANDIDATE[@]} ; i++ ))
do
target=${CANDIDATE[$i]}
echo "restart $target"
scp $CURRENT_HOME/grantSlave.sql root@$target:$SCRIPT_HOME/grantSlave.sql
servernum=$(echo $i+100 | bc)
ssh root@$target "$SCRIPT_HOME/initSlave.sh $MASTER `expr $servernum`"
ssh root@$target "mysql --user="$MYSQL_USERNAME" --password="$MYSQL_PASSWORD" < $SCRIPT_HOME/grantSlave.sql"
ssh root@$target "rm -rf $SCRIPT_HOME/grantSlave.sql"
echo "$target restarts"
done

#finally remove the temp files
rm -rf $CURRENT_HOME/grantSlave.sql*

echo "finish"

