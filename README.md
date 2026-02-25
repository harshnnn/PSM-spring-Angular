# Parcel Management System (Spring Boot + Angular)

Microservices-based Parcel Management System with separate services for authentication and parcel operations, API gateway, and service registry.

## Architecture
- `backend/service-registry` - Eureka service discovery.
- `backend/api-gateway` - Single entry point and CORS.
- `backend/auth-service` - Customer registration/login + hardcoded officer login.
- `backend/parcel-service` - Booking, payment, invoice, tracking, history, pickup scheduling, delivery updates.
- `frontend` - Angular UI for Customer and Officer workflows.

## Tech Stack
- Java 17, Spring Boot, Spring Web, Spring Data JPA, Validation, Lombok, H2
- Spring Cloud Netflix Eureka + Spring Cloud Gateway
- Angular 17 (standalone)

## Officer Credentials
- User ID: `officer01`
- Password: `Officer@123`

## Run Backend
```bash
cd backend
mvn -pl service-registry spring-boot:run
mvn -pl auth-service spring-boot:run
mvn -pl parcel-service spring-boot:run
mvn -pl api-gateway spring-boot:run
```

## Run Frontend
```bash
cd frontend
npm install
npm start
```

## Notes
- Customer and parcel data are persisted in in-memory H2 databases.
- Booking IDs are randomly generated 12-character unique codes.
- Customer acknowledgement shows generated customer code and profile details.
