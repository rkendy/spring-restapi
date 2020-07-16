# spring-restapi

Spring Rest API

## Run
java -jar build\libs\restapi-0.0.1-SNAPSHOT.jar

## Controller
- GreetingController: HATEOAS (GreetingModelAssembler)
- BookController: return ResponseEntity<Book>

## Exception
- Exception Http code (404, 409 etc): exception/\*Advice.java

## Test
Test with Mock: need to add Advice with setControllerAdvice

## Links
- OpenAPI: localhost:8080/api-docs
- Swagger: localhost:8080/swagger-ui.html
