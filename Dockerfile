# Build stage
FROM maven:3-openjdk-11 as build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src/ /build/src
RUN mvn clean install

# Run stage
FROM adoptopenjdk/openjdk11:alpine
WORKDIR /
COPY --from=build /build/target/kanban*.jar kanban.jar
EXPOSE 8080
CMD java -jar kanban.jar
