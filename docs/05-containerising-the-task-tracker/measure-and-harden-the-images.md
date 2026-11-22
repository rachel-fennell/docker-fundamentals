# Measuring/Hardening Images

## Goal
Build a single-stage version of the API image (JDK base, build and run in one stage) and compare its size, layer count and vulnerability scan with the multi-stage image. Prove that a failing unit test stops the image build, and record the image digests of the published 1.0.0 images. 

## Steps taken
1. Added a single-stage JDK-based comparison image in Dockerfile.single
2. Compared the single-stage image with the multi-stage image using docker images and docker history
3. Scanned both images with Trivy
4. Temporarily changed the expected status in TaskServiceTest.java from DONE to OPEN. The image build failed at the test step. Restored the expected value to DONE
5. Recorded the published image digests in the task tracker README

## Commands
docker build -t task-api:single -f task-tracker/task-api/Dockerfile.single task-tracker/task-api

docker images task-api:single
docker images task-api:1.0.0
docker history task-api:single
docker history task-api:1.0.0

docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image --severity HIGH,CRITICAL task-api:single
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image --severity HIGH,CRITICAL task-api:1.0.0

docker compose build task-api

docker images --digests ghcr.io/rachel-fennell/task-api:1.0.0
docker images --digests ghcr.io/rachel-fennell/task-ui:1.0.0

## Results
Single image used more disk space than the multi-stage image:

rachel@BEL-MC-260924B docker-fundamentals % docker images task-api:single
                                                                         i Info →   U  In Use
IMAGE             ID             DISK USAGE   CONTENT SIZE   EXTRA
task-api:single   e75370d479b1       1.29GB          423MB
Row Count: 25


rachel@BEL-MC-260924B docker-fundamentals % docker images task-api:1.0.0
                                                                         i Info →   U  In Use
IMAGE            ID             DISK USAGE   CONTENT SIZE   EXTRA
task-api:1.0.0   5d5dc0f2ae63        632MB          172MB    U
Row Count: 21

Both single-stage and multi-stage had 0 for both HIGh and CRITICAL findings.

The test change made docker compose build fail. After restoring it the API image build passed. 

## Observations

