# A38-Kanban-development

## Short description
This application is a kanban board. In the board you can see, filter and manipulate your gitlab issues. The columns of the board are the possible statuses. Horizontally there are swimlanes, which optionally can be the assignees of the issues or the stories of the issues.

Our application uses OAuth to access the datas in gitlab. We use graphQL API to get the issues from gitlab, and then we show them on a kanban board. 

This is the backend of the application, which is a Spring application. The frontend is written React.  
This is the frontend remote repository: https://github.com/CsapoKrisztian/A38-Kanban-development-Frontend.

## Configuration

Clone this repository and the frontend repostiroy to your local copmuter.
Make sure that the two downloaded directories are on the same level.

For only testing purposes you can use the current configurations. In this case you can use the following credentials at the login part:  
username or email: tothmate911@gmail.com  
password: tothmate911  

http://localhost:3000/getToken

##### Please go to this file: A38 Kanban project/A38-Kanban-development-Frontend/kanban/.env (this is a hidden file, so show hidden files checkbox must be checked to see it)  
In this file Change the listed parameters below:

- REACT_APP_GITLAB_SERVER=https://gitlab.techpm.guru (this should be your own gitlab server)
- REACT_APP_GITLAB_APP_ID=458f27c6eb357cf7419231331e3af3e3a9d39782b7edf50ac2cc083e7a7f1a4a (this should be the app id of your application on your gitlab server)
- REACT_APP_GITLAB_APP_SECRET=f0fbf238c1ef5d0be56bf1118c430b15daff2b85d790d4bbfd76b8ccbb5bac33 (this should be the app sercret of your application on your gitlab server)
- REACT_APP_APPLICATION=http://localhost:3000 (this should be the url of the frontend)


##### Please go to this file: A38 Kanban project/A38-Kanban-development/src/main/resources/application.properties
In this file set the parameters listed below.
- frontend.url=http://localhost:3000 (this should be the frontend url)
- gitlabServerGraphQLApi.url=https://gitlab.techpm.guru/api/graphql (this should be your own gitlab server's graphQL api url, so only change the part of the url before "/api/graphql")

##### Please go to this file: A38 Kanban project/A38-Kanban-development/docker-compose.yml
In this file set the parameters listed below:

- frontend.url=http://localhost:3000 (this should be your frontend url)
- ports: - '3000:3000' (you can set the port here, if you want)

##### Please go to this file: A38 Kanban project/A38-Kanban-development/src/main/resources/configprops.json
In this file you have to set you predefined properties in a Json file.
{  
  "storyPrefix": "Story: ", //(this should be your own story prefix)  
  
  "priorities": [  
    {  
      "title": "Priority: P0", //(this is the label title of one of your priority labels on gitlab)  
      "display": "PO" //(this is what you want to display on the kanban board, that corresponds to the above given title)  
    }  
    ...  
  ],  
  
  "statuses": [  
    {  
      "title": "Backlog", //(this is the label title of one of your status labels on gitlab)  
      "display": "Backlog" //(this is what you want to display on the kanban board, that corresponds to the above given title)  
    }  
  ]  
  
}  
  

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
