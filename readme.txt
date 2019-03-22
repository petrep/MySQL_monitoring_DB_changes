


Useful resources:
https://pusher.com/tutorials/realtime-mysql-java
https://github.com/shyiko/mysql-binlog-connector-java




This tool will use the MySQL binary log file to read DB changes.
(Tested with MySQL version: 14.14 Distrib 5.7.25)

To set up the binary log file:
sudo gedit /etc/my.cnf   *UPDATE:  the correct command is:   sudo gedit /etc/mysql/mysql.conf.d/mysqld.cnf
 and append:
 [mysqld]
 	server-id = 1
	log_bin			= /var/log/mysql/mysql-bin.log
	binlog-format = row
    
    expire_logs_days = 10
    max_binlog_size = 100M
   
 and restart the server to apply the my.cnf changes:
  sudo /etc/init.d/mysql restart

The app will monitor all the databases handled by that MySQL server.


mysql -u root -p
USE somedatabasename
GRANT REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'root'@'localhost';
show master status;   ->  this file be shown in the results:  mysql-bin.000001


	Troubleshooting:
	mysql -u root -p
	show variables;   -> this will show for example the socket file location.
	show variables like "%log_bin%";

	I located my mysqld config file by executing:
	sudo locate mysqld
	Then, I opened each file and I realized that this is the actual config file:
	/etc/mysql/mysql.conf.d/mysqld.cnf

	When adding the binary log file, I found that the server only accepted this value:
	log_bin			= /var/log/mysql/mysql-bin.log    (creating a file in my own home folder would cause server startup failures)


