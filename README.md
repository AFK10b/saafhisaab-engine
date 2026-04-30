# SaafHisaab Engine

**SaafHisaab** ("Clear Accounts") — Backend REST API for micro vendor shift management, sales logging, and automatic cash reconciliation.

> This is the **backend** repository. For the frontend PWA, see [saafhisaab-pwa](https://github.com/AFK10b/saafhisaab-engine/tree/main/saafhisaab-pwa).

---

## What It Does

Provides a secure JWT-authenticated REST API that powers the SaafHisaab mobile app. Vendors open a shift, log sales against menu items, close the shift, and the engine automatically calculates whether the cash in hand matches expected sales — flagging any discrepancy as CLEAR, WARNING, or ALERT.

---

## Tech Stack

| Technology | Purpose |
|------------|---------|
| Java 17 | Programming language |
| Spring Boot 3 | Web framework |
| Spring Security | Route protection and authentication |
| JWT | Stateless token-based auth |
| Spring Data JPA | Database interaction |
| Hibernate | ORM — maps Java classes to DB tables |
| MySQL | Relational database |
| Lombok | Reduces boilerplate (`@Data`, `@Builder`, etc.) |
| Maven | Dependency and build management |

---

## Project Structure

```
saafhisaab-engine/
└── src/main/java/com/saafhisaab/engine/
    ├── controller/
    │   ├── AuthController.java             # Login endpoint
    │   ├── ShiftController.java            # Open, close, active shift
    │   ├── SaleController.java             # Log and view sales
    │   ├── MenuItemController.java         # Menu item CRUD
    │   └── ReconciliationController.java   # Fetch past reports
    ├── service/
    │   ├── ShiftService.java               # Interface
    │   ├── SaleService.java
    │   ├── MenuItemService.java
    │   ├── ReconciliationService.java
    │   └── impl/
    │       ├── ShiftServiceImpl.java       # Shift lifecycle logic
    │       ├── SaleServiceImpl.java        # Sale logging + price calc
    │       ├── MenuItemServiceImpl.java
    │       └── ReconciliationServiceImpl.java  # Core reconciliation engine
    ├── repository/
    │   ├── UserRepository.java
    │   ├── VendorRepository.java
    │   ├── ShiftRepository.java
    │   ├── SaleRepository.java
    │   ├── MenuItemRepository.java
    │   └── ReconciliationReportRepository.java
    ├── entity/
    │   ├── User.java                       # App user (mobile + password)
    │   ├── Vendor.java                     # Shop / business
    │   ├── Shift.java                      # Working session
    │   ├── Sale.java                       # Individual transaction
    │   ├── MenuItem.java                   # Product with price
    │   └── ReconciliationReport.java       # Auto-generated report
    ├── dto/
    │   ├── request/
    │   │   ├── ShiftRequest.java           # Open shift payload
    │   │   ├── CloseShiftRequest.java      # Close shift payload
    │   │   └── SaleRequest.java            # Log sale payload
    │   └── response/
    │       ├── ShiftResponse.java
    │       ├── SaleResponse.java
    │       └── ReconciliationResponse.java
    ├── enums/
    │   ├── ShiftStatus.java                # OPEN, CLOSED
    │   ├── ReconciliationStatus.java       # CLEAR, WARNING, ALERT
    │   └── Role.java                       # OWNER, STAFF
    └── security/
        ├── JwtUtil.java                    # Token generation and validation
        ├── JwtFilter.java                  # Per-request token verification
        └── SecurityConfig.java             # Public vs protected routes, CORS
```

---

## API Endpoints

All endpoints except `/auth/login` require `Authorization: Bearer <token>` header.

### Auth

| Method | Endpoint | Body | Response |
|--------|----------|------|----------|
| POST | `/api/auth/login` | `{ mobile, password }` | `{ token, user }` |

### Shifts

| Method | Endpoint | Body | Response |
|--------|----------|------|----------|
| POST | `/api/shifts/open` | `{ vendorId, openingCash }` | ShiftResponse |
| POST | `/api/shifts/close` | `{ shiftId, closingCash }` | ReconciliationResponse |
| GET | `/api/shifts/active/{vendorId}` | — | ShiftResponse or 204 |

### Sales

| Method | Endpoint | Body | Response |
|--------|----------|------|----------|
| POST | `/api/sales/log` | `{ shiftId, menuItemId, quantity }` | SaleResponse |
| GET | `/api/sales/shift/{shiftId}` | — | List of SaleResponse |

### Menu Items

| Method | Endpoint | Body | Response |
|--------|----------|------|----------|
| GET | `/api/menu-items/vendor/{vendorId}` | — | List of MenuItem |
| POST | `/api/menu-items` | `{ name, price, vendor: { id } }` | MenuItem |

### Reconciliation

| Method | Endpoint | Body | Response |
|--------|----------|------|----------|
| GET | `/api/reconciliation/vendor/{vendorId}` | — | List of ReconciliationResponse |

---

## Reconciliation Logic

When a shift is closed, the engine automatically:

```
totalSales    = SUM of all sale.totalPrice for the shift
expectedCash  = shift.openingCash + totalSales
discrepancy   = expectedCash - shift.closingCash

discrepancy == 0          → CLEAR
|discrepancy| <= ₹50      → WARNING
|discrepancy| >  ₹50      → ALERT
```

---

## Getting Started

### Prerequisites

- Java 17+
- Maven
- MySQL 8+

### Database Setup

```sql
CREATE DATABASE saafhisaab;
CREATE USER 'saafhisaab'@'localhost' IDENTIFIED BY 'yourpassword';
GRANT ALL PRIVILEGES ON saafhisaab.* TO 'saafhisaab'@'localhost';
```

Tables are auto-created by Hibernate on first run.

### Configuration

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/saafhisaab
spring.datasource.username=saafhisaab
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=your_jwt_secret_key
jwt.expiration=86400000
```

### Run

```bash
mvn spring-boot:run
```

API will be available at `http://localhost:8080`

### Build

```bash
mvn clean package
```

---

## Security

- Passwords stored as **BCrypt hashes** — never plain text
- **JWT tokens** expire after 24 hours
- All routes except `/api/auth/login` are protected
- **CORS** configured for frontend origin (`http://localhost:5173`)

---

## MVP Flow

```
POST /auth/login
  → POST /shifts/open
    → GET /menu-items/vendor/{id}
      → POST /sales/log  (repeat)
        → POST /shifts/close
          → GET /reconciliation/vendor/{id}
```

---

## Planned Features

- Role-based access (OWNER vs STAFF)
- Multi-vendor support
- Shift history with filtering
- Analytics endpoints
- Docker support

---

## Related

- [saafhisaab-pwa](../saafhisaab-pwa) — React PWA frontend
