# A38-Kanban-development

## Docker deployment

prerequisite:  
- freshly built jar file has to accessible under target/*.jar  
You can have it by running `mvn clean install` 
- docker installed (https://docs.docker.com/engine/install/)  
- docker-compose installed (https://docs.docker.com/compose/install/)  
- frontend has to accessible under A38-Kanban-development-Frontend/ and be on the same level with the current directory

If he above points are met:

- hit `docker-compose up --build` this will build the docker images from the specified  
docker files based on the `docker-compose.yml` and spin up the application stack.
- Visit `localhost:3000` (To change the host see the docker-compose.yml configuration)  
Be amazed.  
- The `--build` options forces a rebuild every time it is issued. If images are already build
simply hit `docker-compose up`
  
If you want to run the docker compose process in the background just pass the `-d` option to docker-compose