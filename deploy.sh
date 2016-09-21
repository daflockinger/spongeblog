docker stop spongeblog
docker rm spongeblog
docker rmi spongeblog-0.1.0
docker build -f docker/Dockerfile -t spongeblog-0.1.0 .
docker run -d --name spongeblog spongeblog-0.1.0  /bin/bash
