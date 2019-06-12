# Wirecard Backend Challenge

## Prerequisites

### System Information
> Ubuntu 18.04.2 LTS \
Linux 4.15.0-50-generic Ubuntu x86_64 GNU/Linux

### Java 8
> java version "1.8.0_201"
  Java(TM) SE Runtime Environment (build 1.8.0_201-b09)
  Java HotSpot(TM) 64-Bit Server VM (build 25.201-b09, mixed mode)

### Maven
>Apache Maven 3.6.0

### Docker
> Client: \
    Version:           18.09.6 \
    API version:       1.39 \
    Go version:        go1.10.8 \
    Git commit:        481bc77 \
    Built:             Sat May  4 02:35:57 2019 \
    OS/Arch:           linux/amd64
    Experimental:      false

> Server: Docker Engine - Community \
    Engine:
        Version:          18.09.6 \
        API version:      1.39 (minimum version 1.12) \
        Go version:       go1.10.8 \
        Git commit:       481bc77 \
        Built:            Sat May  4 01:59:36 2019 \
        OS/Arch:          linux/amd64 \
        Experimental:     false

### Docker Compose
> docker-compose version 1.23.2, build 1110ad01 \
  docker-py version: 3.6.0 \
  CPython version: 3.6.7 \
  OpenSSL version: OpenSSL 1.1.0f  25 May 2017


### Make (optional)
> GNU Make 4.1 \
  Copyright (C) 1988-2014 Free Software Foundation, Inc.
  
### Operation System Free Ports
> You must release ports to the following services \
    -   MySql -> port 3306 \
    -   Redis -> port 6379 \
    -   Api Rest / Swagger -> port 8089 \
    -   Sonarqube -> port 9000
 

## How to use Application
*Use the following commands on terminal in the root application directory.* 

### - Create Docker Infrastructure - SONARQUBE - MYSQL - REDIS  
###### - Go to "/docker-files/" folder and type:
    make all

    or
  
    docker run -d --name SONARQUBE770 -p 9000:9000 sonarqube && docker-compose up-d

###### - After that, verify if containers are running with command 'docker ps' 

    
__PS: You must wait until MYSQL database start to listener the confgured port!\
This may take a few minutes__

## - Build application with Make or Maven
    make install
    
    or
    
    mvn clean package install -U    
    
## - Start application with Make or Maven
    make runrest
    
    or
    
    mvn spring-boot:run -pl wirecardchallenge-rest
    
__If application fail to start it is probably because the Data Store Infrastructure still not ready or because you have another started instance.__

*With started application.*
## - See API Documentation with Swagger 2
<a href="http://localhost:8089/swagger-ui.html" target="_blank">SpringFox Swagger - http://localhost:8089/swagger-ui.html<a/>

## - See Actuator Information
<a href="http://localhost:8089/actuator" target="_blank">Spring Boot Actuator - http://localhost:8089/actuator<a/>

## - See Sonar Information
<a href="http://localhost:9000" target="_blank">SonarQube - http://localhost:9000<a/> \
User: _admin_ / Password: _admin_

