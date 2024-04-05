   
    docker run \
      --name jenkins-blueocean \(1)
      --restart=on-failure \(2)
      --detach \(3)
      --network jenkins \(4)
      --env DOCKER_HOST=tcp://docker:2376 \(5)
      --env DOCKER_CERT_PATH=/certs/client \
      --env DOCKER_TLS_VERIFY=1 \
      --publish 8080:8080 \(6)
      --publish 50000:50000 \(7)
      --volume jenkins-data:/var/jenkins_home \(8)
      --volume jenkins-docker-certs:/certs/client:ro \(9)
      myjenkins-blueocean:2.440.2-1 (10)
    
