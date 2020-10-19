# Build stage
FROM maven:3-openjdk-11 as build
COPY . /tmp/kanban
RUN mvn -f /tmp/kanban/pom.xml clean install

# Run stage
FROM adoptopenjdk/openjdk11:alpine
WORKDIR /
COPY --from=build /tmp/kanban/target/kanban*.jar kanban.jar
EXPOSE 8080
CMD java -jar kanban.jar