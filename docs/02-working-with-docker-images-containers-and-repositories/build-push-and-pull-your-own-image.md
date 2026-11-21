# Docker images

## Goal
Create a hello-web folder with an index.html and a Dockerfile based on nginx:1.29-alpine. Build and run it, push it to GitHub Container Registry as a public package, delete your local copies and run it again from the registry.

## Steps taken
1. Created hello-web/index.html and hello-web/Dockerfile
2. Built hello-web:1.0.0, ran it on port 8081 and checked the page respons
3. Logged in to GHCR, tagged the image with its registry name, pushed it and made the package public
4. Removed the container and both local image tags, then ran the image from GHCR and checked the page again.
5. Looked at the running container and recorded the image digest, port map and source label

## Commands
docker build -t hello-web:1.0.0 hello-web
docker run -d --name hello -p 8081:80 hello-web:1.0.0
curl http://localhost:8081

echo "$GHCR_TOKEN" | docker login ghcr.io -u rachel-fennell --password-stdin
docker tag hello-web:1.0.0 ghcr.io/rachel-fennell/hello-web:1.0.0
docker push ghcr.io/rachel-fennell/hello-web:1.0.0

docker rm -f hello
docker rmi hello-web:1.0.0 ghcr.io/rachel-fennell/hello-web:1.0.0
docker run -d --name hello -p 8081:80 ghcr.io/rachel-fennell/hello-web:1.0.0
curl http://localhost:8081
docker inspect hello
docker images --digests

## Results
Image was built successfully. local page response returned: Rachel - index html

rachel@BEL-MC-260924B docker-fundamentals % docker build -t hello-web:1.0.0 hello-web                  
[+] Building 4.3s (7/7) FINISHED

rachel@BEL-MC-260924B docker-fundamentals % docker run -d --name hello -p 8081:80 hello-web:1.0.0
a8456ecf522f65abf63214df3800280c1497cf5ba540d80445f89363a82857d1

rachel@BEL-MC-260924B docker-fundamentals % curl http://localhost:8081
Rachel - index html page%

login successful and image pushed:

rachel@BEL-MC-260924B docker-fundamentals % echo "$GHCR_TOKEN" | docker login ghcr.io -u rachel-fennell --password-stdin
Login Succeeded

rachel@BEL-MC-260924B docker-fundamentals % docker tag hello-web:1.0.0 ghcr.io/rachel-fennell/hello-web:1.0.0
docker push ghcr.io/rachel-fennell/hello-web:1.0.0
1.0.0: digest: sha256:195fe2db0e452c81024afdacb1aa28fdc8dc55b3b9ff27a45e4d45a1b997d24b size: 856

After both local image tags were removed, docker reported that the GHCR image was not local so it pulled it and started the container. the GHCR page image returned Rachel - index html.

Status: Downloaded newer image for ghcr.io/rachel-fennell/hello-web:1.0.0
56adc86055062e5f1b55d431b7272179bc4d87ecbfac62fdc22f7c27d5b059cf
Rachel - index html page%

REPOSITORY  TAG   DIGEST  IMAGE ID  CREATED  SIZE
ghcr.io/rachel-fennell/hello-web   1.0.0           sha256:195fe2db0e452c81024afdacb1aa28fdc8dc55b3b9ff27a45e4d45a1b997d24b   195fe2db0e45   12 minutes ago   91.8MB

docker inspect showed container port 80/tcp published on host port 8081

## Observations
first login attempt failed because GHCR_TOKEN was empty. After entering the token at the hidden prompt, login succeeded
token value not included in report.