[![pipeline status](https://gitlab.com/anyalebedenko13/java/badges/main/pipeline.svg)](https://gitlab.com/anyalebedenko13/java/-/commits/main)
[![coverage report](https://gitlab.com/anyalebedenko13/java/badges/main/coverage.svg)](https://gitlab.com/anyalebedenko13/java/-/commits/main)

# java

## Table of contents

* [General info](#general-info)
* [Technologies](#technologies)
* [Configure and run project](#Configure-and-run-project)
* [Run project in Docker](#Run-project-in-Docker)

## General info

This project is my first project written at SPDU courses.

## Technologies

Project is created with:
* spring
* postgresql version: 14.2-1
* flyway version: 8.5.10
* minio version: 8.3.8

## Configure and run project

1. Configure db connection

```properties
    spring.datasource.username=
    spring.datasource.password=
```

```properties
    spring.datasource.url=jdbc:postgresql://${SPRING_DATASOURCE_HOST}:${SPRING_DATASOURCE_PORT}/${SPRING_DATASOURCE_SCHEMA}
```

3. Run postgresql:

```dockerfile
    docker-compose up -d db
```

4. Configure smtp properties for email sending

```properties
    spring.mail.host=localhost
    spring.mail.port=2525
```

5. Set profiles that you need (jpa/jdbc):

```properties
    spring.profiles.active=jdbc
```

5. Start MinIO:

```dockerfile
    docker-compose up -d minio
```

6. Start MailHog

```dockerfile
    docker-compose up -d mailHog
```

7. Build and run project

```shell
    ./gradlew build
     java -jar build/libs/hairdressing-salon-0.0.1-SHAPSHOT.jar
```

## Run project in Docker

```shell
    ./gradlew build
```
```dockerfile
    docker-compose up
```