#!/bin/bash

x=1
while [ $x -le 5 ]
do
  TIMESTAMP=`date +%Y_%m_%d_%H_%M_%S`
  FILENAME="/usr/mysql/backup/winreal-${TIMESTAMP}.sql"

  mysqldump -h mysql -u root -proot erp > ${FILENAME}

  echo "Backup written to ${FILENAME}. Sleep for ${MYSQL_BACKUP_PERIOD} seconds ..."

  sleep $MYSQL_BACKUP_PERIOD
done

