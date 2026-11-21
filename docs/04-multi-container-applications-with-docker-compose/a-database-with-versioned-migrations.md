# DB with version migrations with flyway

## Goal
Create compose-db/ with a compose.yaml defining a PostgreSQL 18 service with a health check and a named volume, and a Flyway 13 service that waits for the database to be healthy and applies migrations from ./migrations. Keep the password in an untracked .env with a committed .env.example, add a psql tool under a profile, and apply three migrations across two runs. 

## Steps taken
1. Created compose-db/compose.yaml with a postgresql 18 service, health check, named volume and flyway 13 service 
2. Created .env.example and local .env and added .env to .gitignore
3. Created migrations V1 and V2, started the compose stack and checked the db health and migration output
4. Queried flyway_schema_history and tasks, added V3, ran flyway again and checked the new migration and seed row
5. Ran docker compose down and up -d to verify the data persisted then did docker compose down -v and up -d to verify the schema and seed data were recreated

## Commands
docker compose config --quiet
docker compose up -d
docker compose ps
docker compose logs -f migrate
docker compose --profile tools run --rm psql

docker compose run --rm migrate
docker compose --profile tools run --rm psql

docker compose down
docker compose up -d
docker compose ps
docker compose --profile tools run --rm psql

docker compose down -v
docker compose up -d
docker compose ps
docker compose --profile tools run --rm psql

## Results
compose completed without error

rachel@BEL-MC-260924B compose-db % docker compose up -d
[+] up 15/15
 ✔ Image flyway/flyway:13         Pulled                                                27.3s
 ✔ Network compose-db_default     Created                                                0.0s
 ✔ Volume compose-db_pgdata       Created                                                0.0s
 ✔ Container compose-db-db-1      Healthy                                                6.1s
 ✔ Container compose-db-migrate-1 Started                                                5.7s

postgres was healthy:
rachel@BEL-MC-260924B compose-db % docker compose ps
NAME              IMAGE         COMMAND                  SERVICE   CREATED         STATUS                   PORTS
compose-db-db-1   postgres:18   "docker-entrypoint.s…"   db        9 seconds ago   Up 9 seconds (healthy)   5432/tcp

flyway applied V1 and V2 successfully:

migrate-1  | Migrating schema "public" to version "1 - create tasks"
migrate-1  | Migrating schema "public" to version "2 - add due date"
migrate-1  | Successfully applied 2 migrations to schema "public", now at version v2 (execution time 00:00.004s)

validated 3 migrations and applied V3 successfully:

rachel@BEL-MC-260924B compose-db % docker compose --profile tools run --rm psql
Container compose-db-psql-run-9b0ed14e9888 Creating
Container compose-db-psql-run-9b0ed14e9888 Created

Successfully validated 3 migrations (execution time 00:00.012s)
Current version of schema "public": 2
Migrating schema "public" to version "3 - seed tasks"
Successfully applied 1 migration to schema "public", now at version v3 (execution time 00:00.004s)

 installed_rank | version | description  | success
----------------+---------+--------------+---------
              1 | 1       | create tasks | t
              2 | 2       | add due date | t
              3 | 3       | seed tasks   | t
(3 rows)

 id |        title        | completed |  due_date
----+---------------------+-----------+------------
  1 | Learn docker cmpose | f         | 2026-10-30
(1 row)

removal of pgdata volume:
rachel@BEL-MC-260924B compose-db % docker compose down -v
[+] down 4/4
 ✔ Container compose-db-migrate-1 Removed                                                0.0s
 ✔ Container compose-db-db-1      Removed                                                0.1s
 ✔ Volume compose-db_pgdata       Removed                                                0.0s
 ✔ Network compose-db_default     Removed                                                0.1s

rachel@BEL-MC-260924B compose-db % docker compose up -d
[+] up 4/4
 ✔ Volume compose-db_pgdata       Created                                                0.0s
 ✔ Network compose-db_default     Created                                                0.0s
 ✔ Container compose-db-db-1      Healthy                                                5.6s
 ✔ Container compose-db-migrate-1 Started                                                5.7s

the named volume retained the task row across a normal down and up. removing the volume with down -v cleared the database and flyway rebuilt it from the migration files

## Observations
