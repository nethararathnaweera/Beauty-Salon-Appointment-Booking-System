# Staff Management Component (Spring Boot)

Complete Java Spring Boot CRUD component for staff management using file persistence (`staff.txt`) instead of a database.

## Tech Stack
- Java 17
- Spring Boot 3
- Maven
- File I/O persistence (`src/main/resources/data/staff.txt`)

## Package
`com.miks.staffmanagement`

## Project Structure
- `controller/` - REST endpoints
- `service/` - business logic
- `repository/` - file persistence logic
- `model/` - staff data model + validation annotations
- `exception/` - global exception handling and API error model

## API Endpoints
- `GET /api/staff` - list all staff
- `GET /api/staff/{id}` - get staff by UUID
- `POST /api/staff` - create staff (`201 Created`)
- `PUT /api/staff/{id}` - update staff
- `DELETE /api/staff/{id}` - delete staff (`204 No Content`)

## Validation Rules
- `name`: required, 2-100 chars
- `role`: required, letters/spaces only, 2-60 chars
- `phone`: required, 7-15 digits, optional leading `+`
- `email`: required, valid email format

## Status Codes
- `201` for successful create
- `204` for successful delete
- `400` for validation errors
- `404` when staff not found

## Run Steps
1. Ensure Java 17 and Maven are installed.
2. Open terminal in project root.
3. Run:

```bash
mvn spring-boot:run
```

App starts on `http://localhost:8080`.

## Sample Request Payload (POST/PUT)

```json
{
  "name": "Dr. Amaya Perera",
  "role": "Lecturer",
  "phone": "+94771234567",
  "email": "amaya.perera@university.edu"
}
```

## Postman Test Examples

### 1) Get all staff
- Method: `GET`
- URL: `http://localhost:8080/api/staff`

### 2) Get staff by id
- Method: `GET`
- URL: `http://localhost:8080/api/staff/{{staffId}}`

### 3) Create staff
- Method: `POST`
- URL: `http://localhost:8080/api/staff`
- Headers: `Content-Type: application/json`
- Body:

```json
{
  "name": "Nilantha Silva",
  "role": "Registrar",
  "phone": "+94770000001",
  "email": "nilantha.silva@university.edu"
}
```

### 4) Update staff
- Method: `PUT`
- URL: `http://localhost:8080/api/staff/{{staffId}}`
- Headers: `Content-Type: application/json`
- Body:

```json
{
  "name": "Nilantha Silva",
  "role": "Senior Registrar",
  "phone": "+94770000002",
  "email": "n.silva@university.edu"
}
```

### 5) Delete staff
- Method: `DELETE`
- URL: `http://localhost:8080/api/staff/{{staffId}}`

## Optional: Postman Tests (Tests tab snippets)

### For Create staff request

```javascript
pm.test("Status is 201", function () {
  pm.response.to.have.status(201);
});
const json = pm.response.json();
pm.environment.set("staffId", json.id);
```

### For Delete staff request

```javascript
pm.test("Status is 204", function () {
  pm.response.to.have.status(204);
});
```

## Unit Tests
Service layer tests are included in:
`src/test/java/com/miks/staffmanagement/service/StaffServiceImplTest.java`

Run tests:

```bash
mvn test
```

## GitHub Collaboration Tips
Recommended commit message sequence:
1. `chore: initialize spring boot staff-management module`
2. `feat: add staff model validation and UUID-based CRUD service`
3. `feat: implement file-based staff repository using staff.txt`
4. `feat: expose staff REST endpoints with correct HTTP status codes`
5. `feat: add global exception handling for validation and not-found`
6. `test: add service layer unit tests with Mockito`
7. `docs: add README with run guide and Postman examples`

