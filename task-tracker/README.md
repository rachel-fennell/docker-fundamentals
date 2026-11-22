# Task Tracker

A task tracker with a Spring Boot API, React UI and PostgreSQL database, run with Docker Compose

## Run the application

Prerequisite: Docker with Compose

From this directory, copy the example environment file and start the stack:

```bash
cp .env.example .env
docker compose up -d --build

Service endpoints for checks
- UI: http://localhost:8081
- UI health: http://localhost:8081/healthz
- API health: http://localhost:8080/actuator/health
- API info: http://localhost:8080/actuator/info
- Tasks through the UI proxy: http://localhost:8081/api/tasks

Add a task through the UI. Tasks persist in the db when the stack is stopped and started again

Stop the application
```bash
docker compose down