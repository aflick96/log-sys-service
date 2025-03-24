# Logistics Service API

## Database & Persistence
- `h2`: Version 2.1.214 (https://github.com/h2database/h2database/releases/download/version-2.1.214/h2-setup-2022-06-13.exe)

## Running the Project
To run the Spring Boot application locally, cd to `log-sys-service` and run the following command:
```mvn clean install spring-boot:run -Dspring-boot.run.profiles=dev```
or set the env variable with ```set SPRING_PROFILES_ACTIVE=dev``` and run ```mvn clean install spring-boot:run```

## H2 Database Console
1. cd to `C:\Program Files (x86)\H2\bin` and run `java -cp h2-2.1.214.jar org.h2.tools.Server` to open the H2 Console
2. In the JDBC Url add the absolute path to the database instance in the project folder
- Example: jdbc:h2:file:C:\Users\aflic\Desktop\API\project\EN.605.789_Project\log-sys-service\src\data\log-db;DB_CLOSE_ON_EXIT=FALSE;
3. In the User Name add `sa`
4. Select Connect to connect

## Swagger
- Available at http://localhost:8080/swagger-ui/index.html
