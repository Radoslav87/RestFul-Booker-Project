API test suite for the public **Restful-Booker** service using **Java 21, Maven, TestNG, Rest-Assured, Allure**.  
The suite covers positive/negative scenarios for **/auth** and **/booking** endpoints and is wired to Jenkins (GitHub Webhook) with Allure reporting.

## Tech stack
- Java 21, Maven 3.9+
- TestNG, Rest-Assured
- Allure (testng + rest-assured adapters)
- Jenkins + Allure Jenkins plugin

  src
└─ test
├─ java/com/restfulbooker
│ ├─ api
│ │ ├─ authorization
│ │ │ └─ PostCreateTokenTests.java
│ │ └─ booking
│ │ ├─ PostCreateBookingTests.java
│ │ ├─ GetBookingTests.java
│ │ ├─ PutUpdateBookingTests.java
│ │ ├─ PatchUpdateBookingTests.java
│ │ └─ DeleteBookingTests.java
│ ├─ steps # “Steps” wrap Rest-Assured calls per endpoint
│ ├─ factories # Request/response DTO factories
│ └─ base # Base test, config, helpers
└─ resources
├─ testing_suites # TestNG suites (api.booking.xml, api.xml, …)
├─ config.properties / data.properties
└─ payloads

Reporting
Local: target/allure-results → allure serve target/allure-results

Jenkins: Allure plugin publishes a “Allure Report” link per build
(results path: target/allure-results).

Jenkins CI:
Freestyle job “Restful-Booker Project” triggered by GitHub Webhook.
