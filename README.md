# SPU - Sustav za prodaju ulaznica

## Preduvjeti

- Java 17
- Maven 4.0.0
- Docker Desktop instaliran i pokrenut

## Baza podataka

PostgreSQL baza podataka automatski se kreira i puni podatcima prilikom pokretanja.

Baza se stvara kroz Docker i uzima se najnoviji image PostgreSQL-a.

U slučaju gašenja aplikacije, Docker kontejner se briše.

H2 baza podataka u memoriji se kreira pri svakom pokretanju testova podatkovnog sloja i integracijskih testova. 

## Pokretanje aplikacije

Pokrenuti Docker Desktop. 

```bash
mvnw spring-boot:run
```

U slučaju različitih operacijskih sustava ili ljusaka potencijalno se treba staviti prefiks `.\` ili `./`.

Aplikacija je dostupna na [http://localhost:8080](http://localhost:8080).

## Pokretanje testova

Testovi se pokreću pokretanjem iduće naredbe:

```bash
mvnw test
```

U slučaju različitih operacijskih sustava ili ljusaka potencijalno se treba staviti prefiks `.\` ili `./`.

## Korišteni alati iz Spring Boot 4.0.5

- Spring Data JPA
- Spring MVC
- Spring Thymeleaf
- Spring Docker Compose
- PostgreSQL driver
- H2 driver
- JUnit
- Mockito