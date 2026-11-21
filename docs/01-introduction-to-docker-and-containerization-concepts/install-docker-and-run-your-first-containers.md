# Running first containers

## Goal
Install Docker for your platform, run the setup checks, then run hello-world, an nginx web server and an Alpine one-off command. Inspect the running container with logs and exec, compare the container's view of the system with your host's, and clean up.

## Steps taken
1. Checked docker engine, docker compose and the hello-world container
2. Ran the nginx web server as a detached container, checked the page at localhost:8080 and viewed its logs
3. Opened a shell in the nginx container and checked its OS release and process list. Checked the host os and process list
4. Ran the alpine one-off command and measured run time
5. Stopped and removed the nginx container then checked for remaining containers and disk usage

## Commands
docker version
docker compose version
docker run --rm hello-world
docker run -d --name web -p 8080:80 nginx:1.29
docker ps
curl http://localhost:8080
docker logs web
docker exec -it web sh

cat /etc/os-release
ps
apt-get update && apt-get install -y procps
ps
exit

time docker run --rm alpine:3.22 echo "hello from alpine"

docker stop web
docker rm web
docker ps -a
docker system df

## Results
Docker version showed both client (Docker Engine 29.9.2 on darwin/arm64) and server (Docker Desktop 4.96.1 with Engine 29.9.2 on linux/arm64). Docker compose showed version v5.6.0. hello world! Printed correctly and installed correctly. 

Nginx container reported Debian GNU/Linux 13 (trixie). host reported macOS 15.8.3. host process list was much larger than container process list. 

rachel@BEL-MC-260924B docker-fundamentals % docker ps -a
CONTAINER ID   IMAGE                         COMMAND                  CREATED       STATUS                   PORTS     NAMES
1a9366c26cca   postgres:18-alpine            "docker-entrypoint.s…"   3 weeks ago   Exited (0) 2 weeks ago             stillword-postgres
6653cc3b0c70   flyway/flyway:latest-alpine   "flyway -url=jdbc:po…"   4 weeks ago   Exited (0) 4 weeks ago             stillword-flyway

rachel@BEL-MC-260924B docker-fundamentals % docker system df
TYPE            TOTAL     ACTIVE    SIZE      RECLAIMABLE
Images          5         2         1.685GB   271.5MB (16%)
Containers      2         0         40.96kB   40.96kB (100%)
Local Volumes   0         0         0B        0B
Build Cache     0         0         0B        0B

Docker stop web and docker rm web both ran correctly. 

Nginx page: 
<!DOCTYPE html>
<html>
<head>
<title>Welcome to nginx!</title>
<style>
html { color-scheme: light dark; }
body { width: 35em; margin: 0 auto;
font-family: Tahoma, Verdana, Arial, sans-serif; }
</style>
</head>
<body>
<h1>Welcome to nginx!</h1>
<p>If you see this page, nginx is successfully installed and working.
Further configuration is required for the web server, reverse proxy,
API gateway, load balancer, content cache, or other features.</p>

<p>For online documentation and support please refer to
<a href="https://nginx.org/">nginx.org</a>.<br/>
To engage with the community please visit
<a href="https://community.nginx.org/">community.nginx.org</a>.<br/>
For enterprise grade support, professional services, additional
security features and capabilities please refer to
<a href="https://f5.com/nginx">f5.com/nginx</a>.</p>

<p><em>Thank you for using nginx.</em></p>
</body>
</html>

The page request went through and returned a http status 200. 



## Observations
The nginx image didn't include ps, so I had to install procps temporarily. the two stopped containers shown by docker ps -a were from earlier work and were left untouched.
