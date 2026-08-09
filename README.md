# EventTicket Management Platform

A high-performance, multi-tenant event ticketing platform built with **Java 21** and **Spring Boot 3.5**, designed to handle event lifecycle management, high-concurrency ticket reservations, asynchronous search indexing, and production-oriented observability.

## ✨ Key Features

* **Event & Venue Management**
  Manage venues, events, sessions, ticket types, and event lifecycle states from `DRAFT` to `PUBLISHED`, `LIVE`, `ENDED`, and `CANCELLED`.

* **High-Concurrency Ticket Reservation**
  Uses **Redis** and **Lua scripts** for atomic inventory operations during high-demand ticket sales, reducing database contention and preventing overselling.

* **Event-Driven Search**
  Publishes domain events through **RabbitMQ** and asynchronously synchronizes event data into **Elasticsearch**, supporting full-text search and n-gram autocomplete.

* **Hexagonal Architecture**
  Separates domain logic from infrastructure concerns using Ports and Adapters, making business rules easier to test, maintain, and evolve.

* **Observability**
  Integrates **Prometheus** and **Grafana** for application and infrastructure monitoring.

* **Externalized Authentication**
  Uses **Keycloak** as an OIDC-compliant identity provider for authentication and authorization.

---

## 🏗️ Architecture

The application follows **Hexagonal Architecture (Ports & Adapters)** combined with domain-oriented modular design.

```text
                         ┌─────────────────────┐
                         │       Client        │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │   REST Controllers  │
                         └──────────┬──────────┘
                                    │
                                    ▼
                    ┌──────────────────────────────┐
                    │      Application Layer       │
                    │                              │
                    │  Use Cases / Application     │
                    │  Services / Ports            │
                    └──────────────┬───────────────┘
                                   │
                                   ▼
                    ┌──────────────────────────────┐
                    │         Domain Layer         │
                    │                              │
                    │ Aggregates / Entities        │
                    │ Domain Services / Policies   │
                    │ Domain Events                 │
                    └──────────────┬───────────────┘
                                   │
                         Ports / Interfaces
                                   │
                 ┌─────────────────┼─────────────────┐
                 ▼                 ▼                 ▼
          ┌────────────┐    ┌────────────┐    ┌────────────┐
          │ PostgreSQL │    │   Redis    │    │ RabbitMQ   │
          └────────────┘    └────────────┘    └─────┬──────┘
                                                     │
                                                     ▼
                                             ┌──────────────┐
                                             │ Elasticsearch│
                                             └──────────────┘
```

The core domain does not depend directly on infrastructure implementations. Infrastructure adapters implement application/domain ports, allowing technologies such as PostgreSQL, Redis, RabbitMQ, and Elasticsearch to be replaced without changing core business rules.

---

## 📦 Module Structure

The project is organized into business-oriented modules:

```text
src/main/java/com/ute/ticket
│
├── event
│   ├── application
│   ├── domain
│   └── infrastructure
│
├── identity
│   ├── application
│   ├── domain
│   └── infrastructure
│
├── organization
│   ├── application
│   ├── domain
│   └── infrastructure
│
├── reservation
│   ├── application
│   ├── domain
│   └── infrastructure
│
├── order
│   ├── application
│   ├── domain
│   └── infrastructure
│
├── ticket
│   ├── application
│   ├── domain
│   └── infrastructure
│
├── search
│   ├── application
│   ├── domain
│   └── infrastructure
│
├── venue
│   ├── application
│   ├── domain
│   └── infrastructure
│
└── shared
    ├── application
    ├── domain
    └── infrastructure
```

### Core Modules

| Module         | Responsibility                                                  |
| -------------- | --------------------------------------------------------------- |
| `event`        | Event aggregates, sessions, ticket types, publishing workflow   |
| `identity`     | User registration and Keycloak integration                      |
| `organization` | Multi-tenant organizations, members, roles                      |
| `venue`        | Venue and physical location management                          |
| `reservation`  | Ticket holding and high-concurrency reservation                 |
| `order`        | Order creation and transactional processing                     |
| `ticket`       | Ticket issuance and lifecycle                                   |
| `search`       | Elasticsearch indexing and event search                         |
| `shared`       | Cross-module events, common infrastructure, shared abstractions |

---

## ⚡ High-Concurrency Reservation

Ticket sales can generate a large number of concurrent requests for a limited inventory.

A traditional approach that directly updates PostgreSQL can create significant database contention:

```text
Users
  │
  │  Thousands of concurrent requests
  ▼
Application
  │
  ▼
PostgreSQL
  │
  ├── Row Locking
  ├── Transaction Contention
  └── Connection Pool Pressure
```

The reservation flow uses Redis as a high-speed inventory layer:

```text
                    Concurrent Requests
                  /    /    |    \    \
                 ▼    ▼     ▼     ▼    ▼
              ┌─────────────────────────┐
              │     Reservation API     │
              └────────────┬────────────┘
                           │
                           ▼
                  ┌─────────────────┐
                  │      Redis      │
                  │                 │
                  │ Atomic Lua      │
                  │ Inventory Ops   │
                  └────────┬────────┘
                           │
                  ┌────────┴────────┐
                  │                 │
             Success            Out of Stock
                  │
                  ▼
             Reservation
```

### Redis + Lua

Inventory operations are executed atomically using Lua scripts.

Conceptually:

```lua
local stock = redis.call('GET', KEYS[1])

if not stock then
    return -1
end

if tonumber(stock) <= 0 then
    return 0
end

redis.call('DECR', KEYS[1])
return 1
```

This allows the inventory check and decrement to execute as one atomic Redis operation, avoiding race conditions such as:

```text
Request A: GET stock = 1
Request B: GET stock = 1

Request A: DECREMENT
Request B: DECREMENT

Result: overselling
```

With the atomic operation, only one request can successfully consume the final inventory unit.

---

## 🔄 Event-Driven Search

Search indexing is decoupled from the core event transaction.

```text
┌───────────────┐
│ Event Service │
└───────┬───────┘
        │
        │ Domain Event
        ▼
┌───────────────┐
│   RabbitMQ    │
└───────┬───────┘
        │
        │ Async Message
        ▼
┌───────────────┐
│ Search Worker  │
└───────┬───────┘
        │
        ▼
┌──────────────────┐
│ Elasticsearch    │
│                  │
│ Full-text Search │
│ N-gram Search    │
│ Autocomplete     │
└──────────────────┘
```

This prevents search indexing from unnecessarily increasing the latency of event management operations.

For example:

```text
Publish Event
     │
     ├── Persist event
     │
     └── Publish EventPublished
                    │
                    ▼
                RabbitMQ
                    │
                    ▼
          Elasticsearch Index
```

The search system can therefore evolve independently from the transactional domain.

---

## 🔐 Authentication & Authorization

Authentication is delegated to **Keycloak 26** using OpenID Connect.

```text
Client
  │
  │ Login
  ▼
Keycloak
  │
  │ Access Token
  ▼
Application
  │
  ├── Authentication
  └── Authorization
```

Organization-level roles include:

* `OWNER`
* `ADMIN`
* `MEMBER`

This provides tenant isolation and role-based access control for organization resources.

---

## 📊 Observability

The platform integrates **Prometheus** and **Grafana** for monitoring.

```text
Application
     │
     │ Metrics
     ▼
Prometheus
     │
     │ Query
     ▼
Grafana
     │
     ├── JVM Metrics
     ├── HTTP Metrics
     ├── Application Metrics
     └── Infrastructure Metrics
```

This makes it possible to monitor system behavior during high-concurrency performance tests and identify bottlenecks such as:

* HTTP latency
* Request throughput
* Error rate
* JVM memory usage
* Database connection pool utilization
* Redis performance
* Application resource usage

---

## 🛠️ Technology Stack

| Category          | Technology             | Purpose                                 |
| ----------------- | ---------------------- | --------------------------------------- |
| Language          | Java 21                | Application development                 |
| Framework         | Spring Boot 3.5        | Application framework                   |
| Database          | PostgreSQL 16          | Transactional persistence               |
| Cache / Inventory | Redis 8                | Caching and atomic inventory operations |
| Search            | Elasticsearch 9.1      | Full-text search and autocomplete       |
| Messaging         | RabbitMQ               | Asynchronous event communication        |
| Authentication    | Keycloak 26            | Identity and access management          |
| Monitoring        | Prometheus             | Metrics collection                      |
| Visualization     | Grafana                | Metrics visualization                   |
| Architecture      | Hexagonal Architecture | Domain/infrastructure separation        |
| Containerization  | Docker Compose         | Local infrastructure                    |

---

## 📁 Infrastructure

The project provides Docker Compose configuration for the main infrastructure dependencies:

```text
Docker Compose
│
├── PostgreSQL
├── Redis
├── Elasticsearch
├── RabbitMQ
├── Keycloak
├── Prometheus
└── Grafana
```

This allows the complete development environment to be started locally without manually installing each infrastructure component.

---

## 🚀 Getting Started

### Prerequisites

Install:

* Java 21
* Maven
* Docker
* Docker Compose

### 1. Clone the repository

```bash
git clone https://github.com/huynhsown/EventTicket.git
cd EventTicket
```

### 2. Start infrastructure

```bash
docker compose up -d
```

Check running containers:

```bash
docker compose ps
```

### 3. Build the application

```bash
./mvnw clean package
```

On Windows:

```powershell
mvnw.cmd clean package
```

### 4. Run the application

```bash
./mvnw spring-boot:run
```

Or:

```bash
java -jar target/*.jar
```

### 5. Access infrastructure

The exact ports can be found in `docker-compose.yaml`.

Typical services include:

```text
PostgreSQL       → 5432
Redis            → 6379
RabbitMQ         → 5672
Elasticsearch    → 9200
Keycloak         → 8080
Prometheus       → 9090
Grafana          → 3000
```

---

## 🧪 Performance Testing

The platform is designed with high-concurrency ticket sales as a primary use case.

Performance testing can be performed using tools such as **JMeter** or **k6**.

A representative test flow is:

```text
                    Load Generator
                   /              \
                  ▼                ▼
              JMeter              k6
                  │                │
                  └───────┬────────┘
                          ▼
                  Reservation API
                          │
                          ▼
                       Redis
                          │
                          ▼
                     Inventory
```

Key metrics to evaluate include:

* Requests per second
* Average latency
* p95 / p99 latency
* Error rate
* Successful reservations
* Inventory consistency
* Database connection utilization
* Redis throughput

---

## 🧠 Design Principles

The project applies several principles commonly used in production backend systems:

### Domain-Driven Design

Business rules are modeled around domain entities, aggregates, domain services, policies, and domain events.

### Hexagonal Architecture

Infrastructure dependencies are isolated behind ports and adapters.

### Event-Driven Architecture

RabbitMQ is used to decouple asynchronous processing such as search indexing from transactional operations.

### Atomic Inventory Operations

Redis Lua scripts provide atomic inventory validation and deduction under high concurrency.

### Separation of Transactional and Search Workloads

PostgreSQL remains the source of transactional data while Elasticsearch serves search-oriented workloads.

---

## 📈 Future Improvements

Potential improvements include:

* Outbox Pattern for reliable event publishing
* Idempotent message processing
* Distributed reservation expiration workers
* Payment workflow with compensation
* Dead-letter queues for failed messages
* Rate limiting for flash-sale traffic
* Distributed tracing with OpenTelemetry
* Horizontal scaling of reservation workers
* More comprehensive load and stress testing

---

## 📚 Documentation

Additional documentation covers:

---

## 📄 License

This project is developed for educational and engineering practice purposes.
