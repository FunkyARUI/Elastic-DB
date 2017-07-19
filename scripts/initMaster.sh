SCRIPT_HOME=/home/ubuntu/elasticDB/scripts

echo "SCRIPT_HOME is set to $SCRIPT_HOME"

MYSQL_HOME=$SCRIPT_HOME/../mysql

echo "MYSQL_HOME is set to $MYSQL_HOME"

source $SCRIPT_HOME/set_env.sh

#rm -rf /var/lib/mysql
#tar xzvf $MYSQL_HOME/mysql-db.tar.gz -C /var/lib
#chown -R mysql.mysql /var/lib/mysql
#chmod -R 777 /var/lib/mysql
sed -e "s/server-id=1/server-id=`expr $1`/ig" $SCRIPT_HOME/my.cnf > /etc/mysql/my.cnf
chown -R mysql.mysql /etc/mysql/my.cnf
chmod -R 644 /etc/mysql/my.cnf
/etc/init.d/mysql start
