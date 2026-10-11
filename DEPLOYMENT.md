um# PathForge – Complete Deployment & Setup Guide

This guide provides step-by-step instructions for configuring, deploying, and connecting **PathForge-Backend** (on Render) and **PathForge-Frontend** (on Vercel).

---

## 📋 Table of Contents
1. [Local Development Setup](#1-local-development-setup)
2. [Step 1: Production MySQL Database Setup](#step-1-production-mysql-database-setup)
3. [Step 2: Deploy Backend to Render](#step-2-deploy-backend-to-render)
4. [Step 3: Configure Frontend API URL](#step-3-configure-frontend-api-url)
5. [Step 4: Deploy Frontend to Vercel](#step-4-deploy-frontend-to-vercel)
6. [Step 5: Verification & Testing](#step-5-verification--testing)

---

## 1. Local Development Setup

### Running Backend Locally
1. Ensure MySQL is running locally on port `3306` with database `pathforge_db`.
2. Build and run the Spring Boot backend:
   ```bash
   mvn clean package
   mvn spring-boot:run
   ```
   *The local REST API will listen at `http://localhost:8080`.*

### Running Standalone H2 Profile (No Local MySQL Required)
If local MySQL is not installed, run with the H2 profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

---

## Step 1: Production MySQL Database Setup

Render Web Services do not include local MySQL by default. You need a reachable managed MySQL instance:

### Option A: Aiven for MySQL (Free Tier Available)
1. Sign up at [Aiven.io](https://aiven.io).
2. Create a **MySQL Service** (free trial or free plan).
3. Copy the service URI: `jdbc:mysql://<HOST>:<PORT>/pathforge_db?sslmode=REQUIRED`
4. Note your database `username` and `password`.

### Option B: Railway / PlanetScale / Clever Cloud
1. Create a MySQL database instance.
2. Run `database/schema.sql` (located in `PathForge-Backend/database/schema.sql`) to initialize tables if DDL auto is set to `none`.

---

## Step 2: Deploy Backend to Render

1. Push your latest `PathForge-Backend` repository to GitHub:
   [https://github.com/bhavyasrimogilicherla123-ai/PathForge-Backend](https://github.com/bhavyasrimogilicherla123-ai/PathForge-Backend)

2. Log in to [Render Dashboard](https://dashboard.render.com/) and click **New +** -> **Web Service**.
3. Connect your GitHub repository `PathForge-Backend`.
4. Choose Deployment Runtime:
   * **Language / Environment:** `Docker`
   * **Dockerfile Path:** `./Dockerfile` (Render will automatically detect the Dockerfile in root).

5. Scroll down to **Environment Variables** and add the following keys:

| Environment Variable Key | Recommended Value | Description |
| :--- | :--- | :--- |
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://<MYSQL_HOST>:<PORT>/<DB_NAME>?useSSL=true` | Production MySQL JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | `<DB_USER>` | MySQL Username |
| `SPRING_DATASOURCE_PASSWORD` | `<DB_PASSWORD>` | MySQL Password |
| `PATHFORGE_CORS_ALLOWED_ORIGINS` | `https://*.vercel.app,http://localhost:5500` | Allowed Frontend domains |
| `PATHFORGE_JWT_SECRET` | `PathForgeSuperSecretKeyForBTechMajorProjectGuidancePlatform2026SecureToken` | JWT signing secret key |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update` | Auto-update database schema safely |

6. Click **Create Web Service**.
7. Once deployed, Render will provide your public backend URL, e.g.:
   `https://pathforge-backend.onrender.com`

---

## Step 3: Configure Frontend API URL

1. Open `PathForge-Frontend/js/config.js`.
2. Replace `'https://pathforge-backend.onrender.com/api'` with your actual Render URL:
   ```javascript
   window.PATHFORGE_API_BASE = isLocalhost
       ? 'http://localhost:8080/api'
       : 'https://YOUR-ACTUAL-RENDER-SERVICE-NAME.onrender.com/api';
   ```
3. Commit and push changes to `PathForge-Frontend` GitHub repository:
   [https://github.com/bhavyasrimogilicherla123-ai/PathForge-Frontend](https://github.com/bhavyasrimogilicherla123-ai/PathForge-Frontend)

---

## Step 4: Deploy Frontend to Vercel

1. Log in to [Vercel Dashboard](https://vercel.com).
2. Click **Add New...** -> **Project**.
3. Import your GitHub repository `PathForge-Frontend`.
4. Settings:
   * **Framework Preset:** `Other` (Static HTML/CSS/JS)
   * **Root Directory:** `./`
5. Click **Deploy**.
6. Vercel will build and assign your production URL (e.g. `https://pathforge-frontend.vercel.app`).

---

## Step 5: Verification & Testing

### Test 1: Health / Public API Check
Open browser or run curl:
```bash
curl -i https://YOUR-RENDER-URL.onrender.com/api/careers
```
*Expected Result:* HTTP status 200 OK returning JSON list of career roles.

### Test 2: Student Registration
1. Navigate to your Vercel frontend URL: `https://pathforge-frontend.vercel.app/register.html`.
2. Fill out Name, Email, Password, Branch, Year, and Semester.
3. Click **Create Account**.
4. *Expected Result:* User registered successfully, JWT token stored in browser `localStorage`, and redirected to `dashboard.html`.

### Test 3: Student Login & Dashboard Navigation
1. Navigate to `https://pathforge-frontend.vercel.app/login.html`.
2. Enter registered credentials or click **Quick Demo Student Login**.
3. *Expected Result:* Returns JWT token and populates Dashboard metrics, Skill Gap Grid, and Dynamic Roadmap.

---

## 🔒 Security & Data Integrity Safeguards

* **Data Preservation:** `DataInitializer` seeds demo skills and career roles ONLY if the skills table is empty. Existing user data is preserved.
* **Non-destructive Migration:** `spring.jpa.hibernate.ddl-auto=update` adds missing tables/columns without dropping existing database tables.
* **JSON Exception Safeguard:** `app.js` checks response `Content-Type` before parsing JSON to prevent `Unexpected token 'T'` syntax errors.
