# Card Cost API

This is a Spring Boot application that manages a clearing cost matrix per country and provides an endpoint to calculate the cost of clearing a given card number using the external Binlist API.

## Features

* CRUD API for managing clearing costs per country
* External API call to [https://lookup.binlist.net](https://lookup.binlist.net) for country lookup based on card number
* Business logic endpoint to return the clearing cost of any given card
* Validation with Bean Validation (Jakarta)
* MapStruct integration for DTO mapping
* Clean separation of domain, service, and resource layers

---

## API Endpoints

### Clearing Cost Management (Admin CRUD)

* `GET /api/clearing-costs` – List all clearing costs
* `GET /api/clearing-costs/{id}` – Get clearing cost by ID
* `POST /api/clearing-costs` – Create a new clearing cost
* `PUT /api/clearing-costs/{id}` – Update a clearing cost
* `DELETE /api/clearing-costs/{id}` – Delete a clearing cost

### Business Logic Endpoint

* `POST /api/given-card-related-cost`

    * Request Body: `{ "cardNumber": "45717360" }`
    * Response: `{ "country": "US", "cost": 5 }`

---

## How It Works

1. The card number is posted to `/api/given-card-related-cost`
2. The app queries `https://lookup.binlist.net/{cardNumber}` to get the country code
3. It looks up the country in the internal clearing cost matrix (DB)
4. If not found, it falls back to country code "OTHERS"
5. Returns the final country and cost as JSON

---

## Technologies Used

* Java 21
* Spring Boot
* Spring Web
* Spring Validation (Jakarta)
* H2 Database — Lightweight in-memory database for development/testing
* MapStruct
* Maven
* RESTful Design
* JUnit 5 — Modern Java testing framework
* Docker — Containerization of the application for portable environments
* JWT (Spring Security) — Stateless auth with role-based access and RSA token signing

---

## Postman Collection

You can find a ready-to-use Postman collection for testing the API under:

[`postman/card-cost-api-collection.json`](postman/card-cost-api-collection.json)

The collection includes:
- A `Login` request to obtain the JWT token
- A pre-configured Bearer Token variable (`{{jwt_token}}`) that is automatically applied to the protected endpoints
- Examples for calling `/api/given-card-related-cost` and `/api/clearing-costs` with authentication
---

## Docker Deployment

This application can be containerized using Docker for easier deployment and scalability.

### Build the Docker Image

```bash
mvn clean package -DskipTests
docker build -t card_cost_api .
```

### Run the Container

```bash
docker run -p 8080:8080 card_cost_api
```

---

### Test the Application


You can test it using tools like Postman or curl and with the embedded unit/integration tests provided.

---


## Authentication Flow (JWT)

All endpoints (except `/auth/login`) are protected and require a valid JWT token.

### How to Authenticate:

1. Send a POST request to the login endpoint:

    ```
    POST /auth/login
    ```

   #### Body (JSON):
    ```json
    {
      "username": "admin1",
      "password": "adminPass"
    }
    ```

2. Copy the `token` from the response:
    ```json
    {
      "token": "eyJhbGciOiJIUzI1NiJ9..."
    }
    ```

3. Use the token as a Bearer token in the `Authorization` header for all subsequent requests:
    ```
    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
    ```

---

## Extendability

The application follows modular architecture with the following layers:

- `domain`: JPA entities
- `service`: business logic
- `representation`: DTOs
- `resource`: REST controllers

The `ClearingCostService` and external lookup logic can be easily extended or swapped via interfaces. Future support for additional card info providers (e.g., RapidAPI) can be added via strategy pattern.

---

## High Availability (HA) Ready

This application is stateless and can be deployed in multiple replicas (e.g. containers) behind a load balancer. No session or in-memory state is required between requests.


---

## Test Coverage

- Unit and integration tests using JUnit 5 and MockMvc
- External API calls (Binlist) are mocked in tests via `@MockBean`
- DB is preloaded via `@Sql(import.sql)` for end-to-end logic testing

---

## License


This project is licensed under the MIT License.

---

Author
----------
Christos Bampoulis  
GitHub: [@ChristosBaboulis](https://github.com/ChristosBaboulis)