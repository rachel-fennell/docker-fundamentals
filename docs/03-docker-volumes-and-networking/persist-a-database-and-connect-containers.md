# DB and containers

## Goal
Create a named volume and a user-defined network, run PostgreSQL 18 on them without publishing its port, write data from a second container, replace the database container, back up and restore the volume, and confirm the data survives each step. Save the commands you run as scripts/03-persist-and-connect.sh.

## Steps taken
1. Created the pgdata volume and appnet network then started postgreSQL 18 as db using the volume and network
2. Connected from a temporary postgreSQL container and created exercise_rows with three rows
3. Removed and recreated db with the same volume then queried the rows
4. Stopped db, backed up pgdata to pgdata.tgz, restored it into pgdata-restored and started postgreSQL as db-restored
5. Connected to db-restored by name and queried the restored rows

## Commands
see scripts/03-persist-and-connect.sh for all commands

## Results
The initial query returned:

 id |  note
----+--------
  1 | first
  2 | second
  3 | third
(3 rows)

same three rows appeared after removing and recreating db with pgdata and again after restoring the backup into pgdata-restored

readiness check stated:
db-restored:5432 - accepting connections

second container reached PostgreSQL using the network name db, and the restored database was reached as db-restored

## Observations
tasks were easy to follow through but i found the information lacking in help for creating the db tables (needed to use -it command). not sure if this is discussed in a different module? 

