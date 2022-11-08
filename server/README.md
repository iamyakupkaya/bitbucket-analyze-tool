*** Bitbucket Tool for Orion Innovation Turkey ***

--> please for setup look A Group
--> please for adding a new repo to code look B Group


** A GROUP
-setups-

1-) MongoDB setup:
--> Download MongoDB on its website: https://www.mongodb.com/try/download/community
--> click next and next buttons to setup MongoDB

2-) Download a Code Editor ( in this part IntellijIdea was used):
--> Download IntellijIdea on its website: https://www.jetbrains.com/idea/download/#section=windows

3-) Download this project on github

4-) Open this project with IntellijIdea and open pom.xml file and update these all dependencies with Maven.

5-) open java/resources/application.properties. İt should be like following. (might be changed, exmp. server.port:8080)
        server.port:8989
        spring.data.mongodb.host=localhost
        spring.data.mongodb.port=27017
        spring.data.mongodb.database=bitbucket
        spring.data.mongodb.url=mongodb://localhost:27017
        spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration

6-) !IMPORTANT! Open helpers directory and EndPointsHepler class and paste your token in following field in Bearer class
        public static final String TOKEN=" your token should be pasted to here"

7-) Connection MongoDB:
--> click MongoDB app on desktop.
--> connect to MongoDB but be careful about 5th step. on MongoDB screen you might find host info.

8-) Run Program.


** B GROUP
-adding a new repo to code-

1-) go to entity package and create a new package with project name. example: asrv

2-) inside the project package which was created by you create a new class like RepoNameEntity

3-) The entity class which created by you inherit from PREntity and add @Document annotation. 
Also can use DatabaseHelper class for collection name. exp: @Document(DatabaseHelper.PR_ASRV_AS_RAF_CORE) 

4-) Go to service package and apply step-1 in B Group and create a interface after interface implement it in implementation package

5-) create a repository for a new entity. repository generics shoul be <EntityClass, String>
exp: public interface McpCoreRootRepository extends MongoRepository<McpCoreRootEntity, String>

6-) Go to service package and find PullRequestServiceImpl class and getPullRequestFromAPI method inside the class and it is ready to apply.
