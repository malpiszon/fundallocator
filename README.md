#FundAllocator 
## General information
FundAllocator is SpringBoot REST app that allocates given amount of money for given Funds using given strategy.

The requested allocation is returned as JSON.

#### API documentation
API documentation: Swagger 2 (even though there is only 1 endpoint :-) ):
```http://localhost:8080/swagger-ui.html```

## Build
Build tool used: gradle (with gradle wrapper). To run:
  * Windows:
    ```bash
    .\gradlew.exe clean bootRun
    ```
  * Linux:
    ```bash
    ./gradlew clean bootRun
    ```
  
## Code quality tools
1. JaCoCo
1. checkstyle
1. findbugs
1. PMD 
 
## Initial data
Initial funds with IDs 1-7 are added to H2 mem database by FlyWay.
