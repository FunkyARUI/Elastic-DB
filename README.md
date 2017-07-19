# elasticDB node setup
1. set master and slaves in set_env.sh
2. make sure you have installed mysql on all the nodes in the queues. 
3. make sure those nodes in the queues have root access to each other without passwd

# elasticDB eclipse
1. import code to your eclipse
2. run mvn eclipse:eclipse and mvn dependency:resolve

# elasticDB property
1. modify the scripts/set_env.sh to set the MASTER, SLAVE and CANDIDATE
2. modify the tpcw.properties to set the read queue, write queue and candiate queues
3. modify the tpcw.properties to set the server that we would like to destroy (to test availability)

# elasticDB experiment setup
1. ./testConnection to test the access of each other
2. ./prepareMasterSlaves to get ready for master, slave and candidates.

# elasticDB run
1. From eclipse, just run without any parameter
2. From eclipse, in order to test scalability run with -c 
3. From eclipse, in order to test availability and scalability run with -c -d
4. You can also run from CommandLine accordingly

# elasticDB monitor
1. run ./enableMonitors.sh, this will run dstats and open windows for MASTER, SLAVE and CANDIDATE
2. open your browser to point to monitorIp:8080/WebContent/elasticdb.jsp

