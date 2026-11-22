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

## Published images

- API: `ghcr.io/rachel-fennell/task-api:1.0.0`  
  Digest: `sha256:5d5dc0f2ae6354c12d345cea9228cbc0bb8a2a1cb1ae3094b62c8eea85efe155`
- UI: `ghcr.io/rachel-fennell/task-ui:1.0.0`  
  Digest: `sha256:af4d1b773a73beafd9dbc80675b218716b1e9f3a81571c3da4e6f6629c2bee21`