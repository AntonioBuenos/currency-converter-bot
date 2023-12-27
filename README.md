# Currency Converter Bot 2.1

This Groovy Spring Pet Project is a Telegram Bot providing different kinds of official currency information (rates,
conversion calculator etc.).
The information provided is based on National Bank of the Republic of Belarus official rates API.
___

### Project stack:

![Apache Groovy](https://img.shields.io/badge/Apache%20Groovy-4298B8.svg?style=for-the-badge&logo=Apache+Groovy&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![Telegram](https://img.shields.io/badge/Telegram-2CA5E0?style=for-the-badge&logo=telegram&logoColor=white)

- Groovy 4;
- JDK 21;
- Spring 6 and Spring Boot 3;
- Spring Data JPA Repositories;
- TelegramBot API;
- Build: Maven;
- Client: Jackson;
- DB: PostgreSQL, Flyway.

___

### Configuration:

You need to add 'application.properties' file to 'resources' folder.
Here's an example:
```
bot.name=<YOUR_BOT_NAME>
bot.token=<YOUR_BOT_TOKEN>
bot.owner=<YOUR_BOT_OWNER_ID>

cron.scheduler=0 0 12 * * MON-FRI

spring.sql.init.mode=always
spring.datasource.hikari.jdbc-url=jdbc:postgresql://localhost:5432/currency_bot
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/currency_bot
spring.datasource.username=<YOUR_DB_USERNAME>
spring.datasource.password=<YOUR_DB_PASSWORD>
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.baseline-description=Flyway Scripts
spring.flyway.user=${spring.datasource.username}
spring.flyway.password=${spring.datasource.password}
spring.flyway.url=${spring.datasource.url}
spring.flyway.default-schema=currency_converter
spring.flyway.out-of-order=true
```