sonar:
	mvn sonar:sonar

test:
	mvn test

package:
	mvn clean package -U

install:
	mvn clean package install -U

install-no-test:
	mvn clean package install -U -DskipTests=true

runrest-install:install
	mvn spring-boot:run -pl wirecardchallenge-rest

runrest-install-no-test:install-no-test
	mvn spring-boot:run -pl wirecardchallenge-rest

runrest:
	mvn spring-boot:run -pl wirecardchallenge-rest

docker:
	docker-compose up -d

