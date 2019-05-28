
package:
	mvn clean package -U

install:
	mvn clean package install -U

install-no-test:
	mvn clean package install -U -DskipTests=true

run-api:install-no-test
	mvn spring-boot:run -pl wirecardchallenge-rest