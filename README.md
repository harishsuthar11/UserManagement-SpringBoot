# UserManagement CRUD SpringBoot
## STEP 1: Setup Project
1. Go to https://start.spring.io/
2. Type Group Name, Artifacts and dependencies.
3. Click on generate project
4. Save project in your workspace
5. Load in  IDE IntelliJ IDEA

## 2.	STEP2: Setup MySQL
1. Create database locally using Mysql workbench.
2. Set below properties in  src\main\resources\application.properties


## 3. STEP 3: Setup Project Structure.
1. Created Packages controller, entity, model, repository, service

## 4. STEP 4: Create Entity

1.Created User class inside Entity package

## 5. STEP 5: Create repository
 1. Created interface UserRepository under repository package, mark notation @Repository and extend it to JpaRepository.
 
 
## STEP 6: Create Service and its implementaion
1.	Create interface UserService under service Package.
2.	Create  methods for CRUD
3.	Create UserServiceImpl and implement UserService interface.


## STEP 7:Create controller class
 1. created UserController class inside controller package and mark it with @RestController
 2. Created all the required End Points POST,GET,PUT,DELETE

