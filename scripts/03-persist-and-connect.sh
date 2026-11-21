docker volume create pgdata
docker network create appnet
docker run -d --name db --network appnet -e POSTGRES_PASSWORD=dev -v pgdata:/var/lib/postgresql postgres:18
docker run --rm --network appnet postgres:18 pg_isready -h db -U postgres

docker run --rm -it --network appnet postgres:18 psql -h db -U postgres

# password set as : dev
# psql prompt:
# CREATE TABLE exercise_rows (id integer PRIMARY KEY, note text);
# INSERT INTO exercise_rows VALUES (1, 'first'), (2, 'second'), (3, 'third');
# SELECT * FROM exercise_rows;

docker rm -f db
docker run -d --name db --network appnet -e POSTGRES_PASSWORD=dev -v pgdata:/var/lib/postgresql postgres:18
docker run --rm --network appnet postgres:18 pg_isready -h db -U postgres
docker run --rm -it --network appnet postgres:18 psql -h db -U postgres

# password set as : dev
# psql prompt:
# SELECT * FROM exercise_rows;

docker stop db
docker run --rm -v pgdata:/data -v "$PWD":/backup alpine:3.22 tar czf /backup/pgdata.tgz -C /data .
docker run --rm -v pgdata-restored:/data -v "$PWD":/backup alpine:3.22 tar xzf /backup/pgdata.tgz -C /data .
docker run -d --name db-restored --network appnet -e POSTGRES_PASSWORD=dev -v pgdata-restored:/var/lib/postgresql postgres:18
docker run --rm --network appnet postgres:18 pg_isready -h db-restored -U postgres
docker run --rm -it --network appnet postgres:18 psql -h db-restored -U postgres

# password set as : dev
# psql prompts:
# SELECT * FROM exercise_rows;
