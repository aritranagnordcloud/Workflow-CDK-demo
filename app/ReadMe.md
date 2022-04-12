# Introduction

This project is a reference implementation using Quarkus, Vert.x web client. The project is built using a Rest API JSON model.

# JSON:API

The APIs conform to the [JSON:API](https://jsonapi.org/format/) specification.

# Usage


## Run application

Quarkus does not need a main method. To run the application, invoke:

`mvn quarkus:dev`

By default, the application will run on port `http://localhost:8080`.

### IntelliJ

The Gradle task `quarkusDev` can be invoked from IntelliJ. To debug, execute Gradle task `quarkusDev` from IntelliJ then
click Run > Attach to Process. It's not possible to debug `quarkusDev` with one click (using debug instead of run).

## Alive end-point

When the web-application is running, the server should respond with an empty 200 OK on the alive end-point:

```shell
curl --location --request GET 'http://localhost:8080/fruits'
```

This end-point is not protected by the API key and does not require a DynamoDB connection.

## Example end-point

When the DynamoDB (docker-compose) and the Quarkus application is running, this end-point should work:

```shell
curl --location --request GET 'http://localhost:8080/fruits' 
```

It should give this response:

```json
[{"name":"Apple","description":"Winter fruit"},{"name":"Pineapple","description":"Tropical fruit"}]
```

If you're having API key issues, make sure the table and entry is present in DynamoDB. Otherwise check LocalStack logs
in Docker and verify LN line-endings (see above).

## Testing

We deliberately avoid using the `@QuarkusTest` annotation. Since we don't use "HTTP based tests", avoiding it will
reduce the startup time of tests significantly.

To read more about HTTP based testing and the other more powerful testing techniques in Quarkus,
see https://quarkus.io/guides/getting-started-testing


# Creating Docker file

Build the jar-file:

```shell
mvn clean package -Dquarkus.package.type=uber-jar
```

Create Docker file (based on Corretto 11):

```shell
docker build . -t sample-rest-api
```

Note that you need to have an AWS region specified when you run the Docker image. Running it on AWS will have it set
automatically but on your local machine you can set it in either `.aws/config` or use `-e AWS_REGION=eu-central-1` or
similar in the docker run command.

Run example:

```shell
docker run -p 8080:8080 -e AWS_REGION=eu-central-1 sample-rest-api
```

# Additional documentation

- https://quarkus.io/guides/
- https://smallrye.io/smallrye-mutiny/index.html
- https://vertx.io/docs/vertx-web-client/java/
- https://jsonapi.org
