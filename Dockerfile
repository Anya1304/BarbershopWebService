FROM openjdk:17-alpine
COPY / /java
WORKDIR /java
RUN chmod 755 gradlew
RUN ./gradlew :bootJar
WORKDIR /java/build/libs
RUN mv *.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]