# EDiagnoise container connectivity

## Goal
Reproduce three broken setups and fix each: an API container on the default bridge network trying to reach db by name, a client using the published host port instead of the container port inside a network, and a client connecting before the database is listening. Use docker network inspect, nslookup and docker logs to find each cause. 

## Steps taken
1. Tried resolving db from the default bridge and from appnet
2. Compared connections to the published host port 15432 and postgreSQLs container port 5432
3. Recreated db with a fresh data directory, checked readiness and reviewed logs

## Commands
docker network inspect appnet
docker run --rm --network bridge alpine:3.22 nslookup db
docker run --rm --network appnet alpine:3.22 nslookup db

docker run --rm --network appnet postgres:18 pg_isready -h db -U postgres
docker run --rm --network appnet postgres:18 pg_isready -h db -p 15432 -U postgres
docker run --rm --network appnet postgres:18 pg_isready -h db -p 5432 -U postgres

docker rm -f db
docker run -d --name db --network appnet -e POSTGRES_PASSWORD=dev -p 15432:5432 postgres:18
time docker run --rm --network appnet postgres:18 sh -c 'until pg_isready -h db -U postgres; do sleep 1; done'
docker logs db

## Results

wrong network
failure evidence: nslookup db on the default bridge returned NXDOMAIN
fix: on appnet, nslookup db resolved to 172.19.0.3 

wrong port
failure evidence: for db:15432 returned no response
fix: pg_isready for db:5432 returned accepting connections. have to use the container port, 5432, between containers on the same network. published host port 15432 is for connections through the host

db not ready
failure evidence: readiness loop first returned db:5432 - no response then db:5432 - accepting connections
fix: wait until readiness succeeds before connecting

appnet
Name:   db
Address: 172.19.0.3

port checks from appnet
db:15432 - no response
db:5432 - accepting connections

readiness loop
db:5432 - no response
db:5432 - accepting connections
time: 1.282 seconds


## Observations
Found this task quite hard to get through with the information provided.

