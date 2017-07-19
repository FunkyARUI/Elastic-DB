GRANT ALL ON *.* TO 'root'@'%' IDENTIFIED BY 'TigerBit!2016';
FLUSH PRIVILEGES;

drop database if exists tpcw;
purge binary logs before now();
create database tpcw;

drop database if exists canvasjs_db;
create database canvasjs_db;
use canvasjs_db;
create table datapoints (x double, u double, r double, w double, m double, s double);
insert into datapoints values
(0,1,1,3,4,10),(1,2,4,27,2,10),(2,3,9,81,1.4,20),(3,4,16,243,1.2,20),(4,5,25,600,1.1,10);

