API test suite for the public **Restful-Booker-https://restful-booker.herokuapp.com/** service using **Java 21, Maven, TestNG, Rest-Assured, Allure**.  
The suite covers positive/negative scenarios for **/auth** and **/booking** endpoints and is wired to Jenkins (GitHub Webhook) with Allure reporting.

## Tech stack
- Java 21, Maven 3.9+
- TestNG, Rest-Assured
- Allure (testng + rest-assured adapters)
- Jenkins + Allure Jenkins plugin

Reporting
Local: target/allure-results → allure serve target/allure-results

Jenkins: Allure plugin publishes a “Allure Report” link per build
(results path: target/allure-results).

Jenkins CI:
Freestyle job “Restful-Booker Project” triggered by GitHub Webhook.
