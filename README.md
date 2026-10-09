# PathForge – Backend REST API

PathForge Backend is a robust Spring Boot 3 & Java 17 REST API service powering the **PathForge** skill guidance platform. It manages user authentication, student profiles, skill gap analysis, personalized roadmap engines, real-world project recommendations, quiz assessments, progress tracking, and YouTube learning analysis.

## 🚀 Key Modules & REST Endpoints

### 1. Authentication (`/api/auth`)
* `POST /api/auth/register` – Register new user (student/admin).
* `POST /api/auth/login` – Login & receive JWT bearer token.

### 2. Student Profile & Goals (`/api/students`)
* `GET /api/students/profile` – Fetch current student profile & career goal.
* `POST /api/students/profile` – Create/Update profile & target career role.

### 3. Skills & Skill Gap Analysis (`/api/skills`)
* `GET /api/skills` – List all platform technical skills.
* `GET /api/skills/gaps` – Analyze skill gap between student scores and career targets.

### 4. Quiz & Assessment (`/api/assessment` & `/api/quizzes`)
* `GET /api/assessment/questions` – Get assessment questions for a skill.
* `POST /api/assessment/submit` – Submit assessment answers & recalculate scores.

### 5. Personalized Roadmaps (`/api/roadmap`)
* `GET /api/roadmap` – Fetch generated dynamic roadmap phases.
* `GET /api/roadmap/today` – Get personalized daily actionable tasks.

### 6. Project Recommendations (`/api/projects`)
* `GET /api/projects/recommendations` – Get recommended projects matched to career role & skills.
* `POST /api/projects/enroll/{id}` – Enroll in a project.
* `POST /api/projects/submit/{id}` – Submit GitHub repo URL for project verification.

### 7. Progress Tracking (`/api/progress`)
* `GET /api/progress` – Retrieve 5-dimension job readiness metrics & overall progress.

### 8. YouTube Video Analysis (`/api/youtube`)
* `POST /api/youtube/analyze` – Extract transcript topics, summary, and skill insights from YouTube links.

---

## 🛠️ Technology Stack

* **Java 17**
* **Spring Boot 3.2.3** (Spring Web, Spring Security, Spring Data JPA, Validation)
* **JWT (JJWT 0.11.5)** for Stateless Authentication
* **MySQL 8.0** (with optional H2 in-memory profile)
* **Maven**

---

## 📁 Directory Structure

```text
PathForge-Backend/
├── database/
│   └── schema.sql                  # MySQL 8.0 DDL Schema script
├── src/
│   ├── main/
│   │   ├── java/com/pathforge/
│   │   │   ├── config/             # SecurityConfig, JwtAuthFilter, DataInitializer
│   │   │   ├── controller/         # REST Controllers
│   │   │   ├── dto/                # Data Transfer Objects
│   │   │   ├── model/              # JPA Entities (User, Skill, StudentProfile, etc.)
│   │   │   ├── repository/         # Spring Data JPA Repositories
│   │   │   └── service/            # Core business logic & assessment engines
│   │   └── resources/
│   │       ├── application.properties      # Primary MySQL database configuration
│   │       └── application-h2.properties   # H2 Standalone profile
│   └── test/
├── pom.xml
├── .gitignore
└── README.md
```

---

## ⚙️ Configuration & Setup

### 1. Database Setup (MySQL)
Execute `database/schema.sql` on your MySQL server:
```sql
CREATE DATABASE pathforge_db;
```
Configure your database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pathforge_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

### 2. Standalone H2 Profile (No MySQL Required)
To run using H2 in-memory database:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

### 3. Build & Run
Compile and run using Maven:
```bash
mvn clean package
mvn spring-boot:run
```
The REST API server will run on `http://localhost:8080`.

---

## 🌐 CORS & Security

CORS is pre-configured in `SecurityConfig.java` to allow cross-origin HTTP requests from standalone frontend instances (such as VS Code Live Server on `http://localhost:5500` or custom origins).
