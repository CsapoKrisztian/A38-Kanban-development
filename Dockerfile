FROM adoptopenjdk/openjdk11:alpine
WORKDIR /
ADD target/kanban*.jar kanban.jar
EXPOSE 8080
CMD java -jar kanban.jar