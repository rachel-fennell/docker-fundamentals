# Containerise and publish task tracker

## Goal
Create task-tracker/ in your course repository with the API and UI sources from this module, multi-stage Dockerfiles, nginx.conf, .dockerignore files, compose.yaml, .env.example and a README. Start the stack, verify the health, info and proxied API endpoints and the UI, then push task-api:1.0.0 and task-ui:1.0.0 to GitHub Container Registry as public packages. 

## Steps taken
1. Generated the spring boot API then added the task entity, repository, service, controller, app configuration, database migration and unit tests from provided code
2. Created the react and TS UI, including the task helper and its test from provided code
3. Added the API dockerfile, UI dockerfile, nginx configuration and .dockerignore files. Added compose configuration and .env.example. Built and started the stack with docker compose. 
4. Checked the API health and info endpoints, the task list through the UI proxy and the UI in a browser. Added a task and confirmed it remained after stopping and restarting 
5. Confirmed the API runs as a non-root user. Temporarily changed the unit-test expectation to make the image build fail at the test step (assertThat(done.status).isEqualTo("DONE"); changed to assertThat(done.status).isEqualTo("GONE"); ). Then restored the expected value and rebuilt successfully
6. Tagged and pushed the API and UI images to GHCR, made both packages public and added the task tracker README

## Commands
mkdir task-tracker && cd task-tracker
curl -s https://start.spring.io/starter.tgz \
  -d type=gradle-project-kotlin -d language=java -d javaVersion=25 -d bootVersion=4.1.2 \
  -d groupId=com.example -d artifactId=task-api -d name=task-api \
  -d packageName=com.example.taskapi -d baseDir=task-api \
  -d dependencies=web,actuator,data-jpa,postgresql,flyway,validation | tar -xzf -
rm task-api/src/test/java/com/example/taskapi/TaskApiApplicationTests.java

npm create vite@latest task-ui -- --template react-ts
cd task-ui
npm install --include=dev react@19.3 react-dom@19.3 && npm install --include=dev -D @types/react@19 @types/react-dom@19 typescript@7 vitest@5
npm pkg set scripts.test="vitest run"
cd ..

npm install -D oxlint
npm run lint


cp .env.example .env            
docker compose up -d --build
curl http://localhost:8080/actuator/health     
curl http://localhost:8080/actuator/info       
curl http://localhost:8081/api/tasks           

docker compose down
docker compose up -d
curl http://localhost:8081/api/tasks
docker compose exec task-api id

docker compose build task-api

for app in task-api task-ui; do
  docker tag $app:1.0.0 ghcr.io/rachel-fennell/$app:1.0.0
  docker push ghcr.io/rachel-fennell/$app:1.0.0
done

## Results
UI unit test passed:
Test Files  1 passed (1)
      Tests  1 passed (1)
   Start at  14:09:34
   Duration  748ms (transform 48%, import 28%, worker 20%, tests 4%)

Endpoint checks:
rachel@BEL-MC-260924B task-tracker % curl http://localhost:8080/actuator/health
{"groups":["liveness","readiness"],"status":"UP"}%                                           rachel@BEL-MC-260924B task-tracker % curl http://localhost:8080/actuator/info
{"app":{"version":"1.0.0"}}%                                                                 rachel@BEL-MC-260924B task-tracker % curl http://localhost:8081/api/tasks
[{"completedAt":null,"dueDate":"2026-11-25","id":1,"status":"OPEN","title":"Write the README"},{"completedAt":null,"dueDate":"2026-11-29","id":2,"status":"OPEN","title":"Containerise the API"}]%

Added in third task from UI browser screen and restarted stack. tasks response contained new task: 
rachel@BEL-MC-260924B task-tracker % docker compose down
[+] down 4/4
 ✔ Container task-tracker-task-ui-1  Removed                                             0.2s
 ✔ Container task-tracker-task-api-1 Removed                                             0.2s
 ✔ Container task-tracker-db-1       Removed                                             0.1s
 ✔ Network task-tracker_default      Removed                                             0.1s
rachel@BEL-MC-260924B task-tracker % docker compose up -d
[+] up 4/4
 ✔ Network task-tracker_default      Created                                             0.0s
 ✔ Container task-tracker-db-1       Healthy                                             5.7s
 ✔ Container task-tracker-task-api-1 Started                                             5.7s
 ✔ Container task-tracker-task-ui-1  Started                                             5.8s
rachel@BEL-MC-260924B task-tracker % curl http://localhost:8081/api/tasks
[{"completedAt":null,"dueDate":"2026-11-25","id":1,"status":"OPEN","title":"Write the README"},{"completedAt":null,"dueDate":"2026-11-29","id":2,"status":"OPEN","title":"Containerise the API"},{"completedAt":null,"dueDate":null,"id":3,"status":"OPEN","title":"starting at unosquare"}]%

Changed the test and caused it to fail:
13.74 > Task :test FAILED
13.74
13.74 TaskServiceTest > completingATaskMarksItDoneAtTheCurrentTime() FAILED
13.74     org.opentest4j.AssertionFailedError at TaskServiceTest.java:24
13.74
13.74 1 test completed, 1 failed
13.74
13.74 FAILURE: Build failed with an exception.

Both pushes to GHCR complete:
The push refers to repository [ghcr.io/rachel-fennell/task-api]
cedc468d06b2: Pushed
e71bc7baa40e: Pushed
44136fa355b3: Mounted from rachel-fennell/hello-web
7f2c2ca81066: Pushed
889aeee7f74a: Pushed
ae1208af986e: Pushed
4f4fb700ef54: Pushed
f1f299755150: Pushed
9f7440fefddc: Pushed
a84be93a2267: Pushed
d39959c9eab1: Pushed
The push refers to repository [ghcr.io/rachel-fennell/task-ui]
6ba4395e953c: Pushed
44136fa355b3: Mounted from rachel-fennell/task-api
a9986cd6f37d: Pushed
04fa1e646fc9: Pushed
0cd2dfaf53a0: Pushed
a13b50162360: Pushed
994fbca5f970: Pushed
0bb23ae5b1cb: Pushed
aa7a6bd1f4f9: Pushed
a2d67277492f: Pushed
9d39ace23ce6: Pushed
9a9a56e17deb: Pushed
09145fc1698b: Pushed

## Observations
Easy to follow with the instructions and code provided. Ran into some difficulty when downloading react ts template it asks whether to use eslint or oxlint and wasn't sure what to select. Selected eslint but didn't work with TS dependency needed so had to change to oxlint. Also had an old container (hello) still running on port 8081 so had to close that in order to get the API to load properly

