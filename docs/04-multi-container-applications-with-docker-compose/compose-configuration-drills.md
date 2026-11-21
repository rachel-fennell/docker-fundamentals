# Congif drills

## Goal
Add a compose.override.yaml that publishes the database on port 5432 for local tools, and a compose.ci.yaml that sets a different password variable and no published ports. Use docker compose config to show the resolved file for each combination, and break the .env deliberately to see the ${DB_PASSWORD:?} error.

## Steps taken
1. Added compose.override.yaml to publish the database on port 5432
2. Compared the auto merged local config with the base config then added compose.ci.yaml to use CI_DB_PASSWORD without publishing a port
3. Created a separate PostgreSQL-only stack in compose-db-secret/ using POSTGRES_PASSWORD_FILE and a db_password secret
4. temporarily renamed .env and ran compose config to confirm the required DB_PASSWORD check fails

## Commands
docker compose config
docker compose -f compose.yaml config
docker compose -f compose.yaml -f compose.ci.yaml config
docker compose config
docker compose up -d
docker compose ps
docker compose down

## Results
base only config had no published port. default config included port mapping for PostgreSQL: host port 5432 to container port 5432. ci config also had no published port and used both CI_DB_PASSWORD for both postgresql and flyway.

this shows that the db was healthy and no published port in secret copy:

rachel@BEL-MC-260924B compose-db-secret % docker compose up -d

[+] up 3/3
 ✔ Network compose-db-secret_... Created 0.0s
 ✔ Volume compose-db-secret_p... Created 0.0s
 ✔ Container compose-db-secre... Started 0.1s
rachel@BEL-MC-260924B compose-db-secret % docker compose ps
NAME                     IMAGE         COMMAND                  SERVICE   CREATED         STATUS                   PORTS
compose-db-secret-db-1   postgres:18   "docker-entrypoint.s…"   db        7 seconds ago   Up 6 seconds (healthy)   5432/tcp
rachel@BEL-MC-260924B compose-db-secret % docker compose down
[+] down 2/2
 ✔ Container compose-db-secre... Removed 0.1s
 ✔ Network compose-db-secret_... Removed 0.1s


when .env was missing, compose failed with: 
rachel@BEL-MC-260924B compose-db % docker compose config
WARN[0000] The "DB_PASSWORD" variable is not set. Defaulting to a blank string.
WARN[0000] The "DB_PASSWORD" variable is not set. Defaulting to a blank string.
error while interpolating services.db.environment.POSTGRES_PASSWORD: required variable DB_PASSWORD is missing a value: Set DB_PASSWORD in .env



## Observations
Quite hard to understand the questions but once i figured it out, the task was easy to complete. 

