----------------------------------------------------
###Technologies:
* Maven
* Hibernate
* Spring Boot
* Docker
* Swagger - OpenApi
----------------------------------------------------

Requirements:
* PostgreSQL with a database
* Maven
* JDK 17

----------------------------------------------------
###Swagger UI :
[localhost 8181 link]
(http://localhost:8181/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config)

----------------------------------------------------
###Getting Started
1. Installation process for development environment:
   How to create a PostgreSQL database in Docker:
   1. Have your Docker Desktop up and running
   2. Open your terminal (for example command prompt in Windows)
   3. Type `docker run --name postgresqldb -e POSTGRES_PASSWORD=postgres -d -p 5432:5432 postgres`
      Press enter, make sure that you got a running container with the name "postgres" in your Docker Desktop
      4.Type `docker ps` you will see the container id of your Docker container, copy it
      5.Type `docker exec -it b3bee3c04a62 bin/bash`, but put your container id instead of b3bee3c04a62
   4. Type `psql -U postgres`
   5. Type `CREATE DATABASE article;` Here it´s important to not forget `;`. You will see a message `CREATE DATABASE`
   6. Type `\c article` you will see a message You are now connected to database `postgresdb` as user `postgres`.
   7. Run your article project in your IDE, Spring Boot will connect to you database and create a table `article`
      Now we created our database called article in a Docker container.
2. docker exec postgresqldb psql -U postgres -d postgres -c "DROP DATABASE article WITH (FORCE);"
3. docker exec postgresqldb psql -U postgres -d postgres -c "CREATE DATABASE article;"

      