# PeterHaviland.dev
This my personal website.

## Stack
**Web Framework:** Spring MVC  
**Template Engine:** Thymeleaf  
**Database:** MongoDB  
**Persistence Framework:** Morphia  
**Project Management:** Maven

## How to Run Locally
```
mvn spring-boot:run
```
The URL is: https://localhost:5000/  
An environment variable is needed for the MongoDB connection string: (Variable=CONNECTION_STRING, Value=*the connection string*)

## How to Build
```
mvn clean package -P prod
```
