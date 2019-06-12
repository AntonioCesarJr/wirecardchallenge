sonarqube:
	docker run -d --name SONARQUBE770 -p 9000:9000 sonarqube

mysqlredis:
	docker-compose up -d

all:
	docker run -d --name SONARQUBE770 -p 9000:9000 sonarqube && docker-compose up -d
