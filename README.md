# spring-restapi

Spring Rest API

java -jar build\libs\restapi-0.0.1-SNAPSHOT.jar

GreetingController: HATEOAS (GreetingModelAssembler)

BookController: return ResponseEntity<Book>

Exception Http code (404, 409 etc): exception/\*Advice.java
Test with Mock: need to add Advice with setControllerAdvice

OpenAPI:
localhost:8080/api-docs
Swagger:
localhost:8080/swagger-ui.html
