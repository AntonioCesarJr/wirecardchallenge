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


### Make
> GNU Make 4.1 \
  Built for x86_64-pc-linux-gnu \
  Copyright (C) 1988-2014 Free Software Foundation, Inc.

## How to use Application

### 1 - Create Docker Infrastructure - MYSQL and REDIS  
    docker-compose up -d
__PS: You must wait until MYSQL database start to listener the confgured port!\
This may take a few minutes__

## 2 - Build application 
    make install
    
## 3 - Start application 
    make run-api
    
__If application fail to start it is probably because the Data Store Infrastructure still not ready or because you have another started instance.__

## 4 - See API Documentation with Swagger 2
<a href="http://localhost:8089/swagger-ui.html" target=_blank>SpringFox Swagger - http://localhost:8089/swagger-ui.html<a/>

## 5 - See Actuator Information
<a href="http://localhost:8089/actuator" target=_blank>Spring Boot Actuator - http://localhost:8089/actuator<a/>

