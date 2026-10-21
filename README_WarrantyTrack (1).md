# WarrantyTrack

WarrantyTrack is a Spring Boot project intended to help users keep track
of product warranty information in one place. It provides a web
interface/dashboard and a backend that can be used to manage warranty
records. Confirm the exact fields, endpoint paths, database, and startup
commands against the submitted source code before final submission.

## Features

-   Maintain warranty information for products.
-   View warranty records through the project interface.
-   Use a Spring Boot backend organized into controller, service,
    repository, and model/entity layers, where implemented.
-   Persist records using Spring Data JPA and a configured database,
    where enabled.
-   Provide REST endpoints for create, read, update, and delete
    operations, where implemented.

> **Submission accuracy note:** This README is written from the project
> name and supplied evaluation rubric. Replace any example or
> placeholder below with the exact values used in your source code. Do
> not claim an endpoint, validation rule, or database is implemented
> unless you have verified it.

## Technology Stack

Update this list to match `pom.xml` and the project files.

-   Java: `<version from pom.xml>`
-   Spring Boot: `<version from pom.xml>`
-   Spring Web: REST API / web layer, if included
-   Spring Data JPA: persistence layer, if included
-   Database: `<MySQL / PostgreSQL / H2 / other configured database>`
-   Frontend: HTML, CSS, and JavaScript if the dashboard files are
    included
-   Testing/API tools: JUnit, Postman, or Swagger/OpenAPI, if configured

## Project Structure

A typical layered layout is shown below. Keep only folders that actually
exist in your repository.

``` text
WarrantyTrack/
├── src/
│   ├── main/
│   │   ├── java/<your-package>/
│   │   │   ├── controller/    # HTTP endpoints
│   │   │   ├── service/       # Business rules
│   │   │   ├── repository/    # Database access
│   │   │   ├── model/         # Entity/domain classes
│   │   │   └── ...            # Application/configuration classes
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/        # Dashboard files, if stored here
│   └── test/
├── pom.xml
└── README.md
```

## Prerequisites

1.  Install the JDK version required by the project.
2.  Install Maven, or use the Maven Wrapper if the repository includes
    `mvnw`/`mvnw.cmd`.
3.  Install and start the configured database if the application uses an
    external database.
4.  Clone or download the project source.

## Configuration

Open `src/main/resources/application.properties` (or `application.yml`)
and check the actual settings. For a database-backed project, configure
the database URL, username, password, and JPA settings appropriate to
your local environment.

Example only --- replace with your project's real database and
credentials; never commit real passwords:

``` properties
spring.datasource.url=jdbc:mysql://localhost:3306/warrantytrack
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
```

The example above assumes MySQL and is **not** a claim that the project
uses MySQL. Use the driver and properties that match your configuration.
If the project uses H2 or another database, document that instead.

## Build and Run

From the project root, use the command matching your setup:

``` bash
mvn clean install
mvn spring-boot:run
```

If the Maven Wrapper is included, use `./mvnw clean install` and
`./mvnw spring-boot:run` on macOS/Linux, or `mvnw.cmd clean install` and
`mvnw.cmd spring-boot:run` on Windows.

Alternatively, run the main class annotated with
`@SpringBootApplication` from your IDE. Check the console for the actual
port and startup status. The default Spring Boot port is commonly
`8080`, but your configuration may differ.

## API Documentation

Fill in this table with the exact mappings from your `@RestController`
classes. The paths below are placeholders, not verified routes.

  -------------------------------------------------------------------------------------
  Operation      HTTP method    Endpoint                  Request body   Expected
                                                                         result
  -------------- -------------- ------------------------- -------------- --------------
  Create a       POST           `<actual create route>`   Warranty JSON  Created record
  warranty                                                               and
                                                                         appropriate
                                                                         status

  List           GET            `<actual list route>`     None           Collection of
  warranties                                                             records

  Get one        GET            `<actual detail route>`   None           One record, or
  warranty                                                               not-found
                                                                         response

  Update a       PUT/PATCH      `<actual update route>`   Updated        Updated record
  warranty                                                warranty JSON  

  Delete a       DELETE         `<actual delete route>`   None           Successful
  warranty                                                               deletion
                                                                         response
  -------------------------------------------------------------------------------------

If Swagger/OpenAPI or a Postman collection is present, link it here and
explain how to open or import it. Document the actual response codes and
JSON fields after testing.

### Example request body

This is an illustrative shape only. Replace property names and values to
match the entity/DTO in your code.

``` json
{
  "productName": "Laptop",
  "purchaseDate": "2026-01-15",
  "warrantyPeriodMonths": 24
}
```

## Architecture and Request Flow

The intended layered flow for a Spring Boot REST application is:

``` text
Client / Dashboard
       |
       v
Controller (@RestController)
       |
       v
Service (@Service)
       |
       v
Repository (JpaRepository)
       |
       v
Database
```

-   **Controller:** accepts HTTP requests, validates or maps input, and
    returns HTTP responses.
-   **Service:** contains application/business logic and coordinates
    data operations.
-   **Repository:** provides persistence operations through Spring Data
    JPA when used.
-   **Entity/model:** represents the warranty data stored or handled by
    the application.
-   **Database:** stores records when persistence is configured.

Keep this diagram aligned with the classes and packages in the submitted
source.

## Testing

Record only tests actually performed. Recommended checks:

-   Build succeeds without compilation errors.
-   Create a warranty and confirm the response and persisted record.
-   Retrieve the created record.
-   Update the record and verify the changed values.
-   Delete the record and verify the result.
-   Submit an invalid or unknown ID and verify the application's error
    response.
-   If available, run automated tests with `mvn test`.
-   Save screenshots or a Postman collection as evidence if required by
    the submission rubric.

## Troubleshooting

-   **Application fails to start:** check the Java version, Maven
    output, and port conflicts.
-   **Database connection fails:** verify the database is running and
    the URL, driver, username, and password are correct.
-   **404 response:** compare the request URL and HTTP method with the
    controller mappings.
-   **400 response:** check JSON property names, data types, and
    validation constraints.
-   **No data after restart:** check the configured database and
    persistence settings.
