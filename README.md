# docker-fundamentals

A public course repository covering Docker images, containers, volumes, networking, Compose and a containerised Task Tracker application

## Prerequisites

- Git
- Docker Desktop or Docker Engine with Docker Compose
- `curl` for the endpoint checks

## Run the Task Tracker

From a fresh clone:

```bash
git clone https://github.com/rachel-fennell/docker-fundamentals.git
cd docker-fundamentals/task-tracker
cp .env.example .env
docker compose up -d --build
```

The build runs the API and UI tests. Check the services:

```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8080/actuator/info
curl http://localhost:8081/api/tasks
```

Open the UI at http://localhost:8081

Stop the stack while keeping its database volume:

```bash
docker compose down
```

See task-tracker/README.md for more details

## Course evidence and module tags

01 — Introduction to Docker and containerization concepts
[Module 01 evidence](docs/01-introduction-to-docker-and-containerization-concepts/)
[module-01](https://github.com/rachel-fennell/docker-fundamentals/tree/module-01)

02 — Working with Docker images, containers and repositories
[Module 02 evidence](docs/02-working-with-docker-images-containers-and-repositories/)
[module-02](https://github.com/rachel-fennell/docker-fundamentals/tree/module-02)

03 — Docker volumes and networking
[Module 03 evidence](docs/03-docker-volumes-and-networking/)
[module-03](https://github.com/rachel-fennell/docker-fundamentals/tree/module-03)

04 — Multi-container applications with Docker Compose
[Module 04 evidence](docs/04-multi-container-applications-with-docker-compose/)
[module-04](https://github.com/rachel-fennell/docker-fundamentals/tree/module-04)

05 — Containerising the Task Tracker
[Module 05 evidence](docs/05-containerising-the-task-tracker/)
[module-05](https://github.com/rachel-fennell/docker-fundamentals/tree/module-05)