## Java Continuous Delivery by Docker

[![Circle CI](https://circleci.com/gh/tecris/continuous-delivery.svg?style=svg)](https://circleci.com/gh/tecris/continuous-delivery)
[![Build Status](https://travis-ci.org/tecris/continuous-delivery.svg?branch=master)](https://travis-ci.org/tecris/continuous-delivery)

Continuous delivery demo that aims to use following principles:
 - immutable environments
 - short lived environments 
  - **built and destroyed** with every integration test run

<hr/>
- [**Continuous delivery with Jenkins (on-prem)**](#continuous-delivery-with-jenkins)
- [**E2E with Docker Compose and Maven**](#e2e-with-docker-compose-and-maven)
- [**Swagger**](#swagger)
- [**Kubernetes**](#kubernetes)

<hr/>

### Stack
| *Technology* | *Version* |
| ------------- | ------------- |
| Java | 8 |
| Wildfly | 10.0.0 |
| MySQL | 5.7 |
| Maven | 3.3 |
| Docker | 1.10 |
| Docker Compose | 1.6 |

### Continuous delivery with Jenkins
 - Configure [private registry](https://github.com/tecris/docker/tree/v16.02.01/registry2/private)
 - Enable Docker Remote API edit `/etc/default/docker` and update the DOCKER_OPTS:
  * `DOCKER_OPTS='-H tcp://0.0.0.0:4243 -H unix:///var/run/docker.sock ...'`
 - `$ ./startCdInfrastructure.sh`
 - `$ cd jenkins && ./createJob.sh localhost planets config.xml`
 - http://localhost:8088/job/continuous-delivery -> 'Build Now'

##### Continuous Delivery life-cycle
 - Build war artifact
 - Upload artifact to maven repository
 - Start vanilla MySQL
 - Apply db schema [flywaydb.org](flywaydb.org)
 - Create web application image with latest war artifact
 - Push web application image to private docker registry
 - Start web application container
 - Execute integration tests
 - Stop and remove web and database containers

 
### E2E with docker compose and Maven
  * **Step-by-step**

    ```sh
      $ docker-compose up -d ackris-db ackris-web  # start web and database containers
      $ mvn clean compile flyway:migrate           # deploy database schema
      $ mvn clean wildfly:deploy                   # deploy application
      $ mvn clean integration-test                 # run integration tests
    ```

Use `-Dmaven.buildNumber.doCheck=false` if project contains local changes

Demo: `http://localhost:8080/bookstore`

#### Swagger

   * `$ docker-compose up -d swaggerui`
 
   * [Swagger UI](https://github.com/swagger-api/swagger-ui) @ `http://localhost:81`


#### Kubernetes

 * Assumptions: 
  
  - [**kubernetes installed**](https://github.com/tecris/kubernetes/blob/v16.02.02/coreos-libvirt/README.md)
  - [**Continuous delivery with Jenkins**](#continuous-delivery-with-jenkins) step executed(2 builds are required to execute rolling update demo).

 * Deploy

   ```$ ./deployToKubernetes.sh```
   
 * Rolling update
 
   ```
   $ kubectl scale --replicas=2 rc planets-web-rc-v1        # scale to 2 pods (optional)     
   
   # Replication Controller rolling update from "planets-web-rc-v1" to "planets-web-rc-v2".
   # (image change from blue.sky/planets-web:1 to blue.sky/planets-web:2)
   $ kubectl rolling-update planets-web-rc-v1 planets-web-rc-v2 -f kubernetes/planets-web-rc-v2.yaml    
   ```
   `http://192.168.122.51:30002/bookstore`


 *  Varia kubectl commands
 
   ```
    $ kubectl describe pod pod_name
    $ kubectl delete pod pod_name
    $ kubectl get rc
    $ kubectl describe rc planets-web-rc-v1
    $ kubectl delete rc planets-web-rc-v1
    $ kubectl delete svc planets-web-svc
   ```


[1]:https://github.com/fabric8io/docker-maven-plugin
[2]:http://flywaydb.org
[3]:https://github.com/tecris/docker/blob/v3.6/nexus/README.md
[4]:https://github.com/tecris/docker/blob/v3.6/nexus/settings.xml
