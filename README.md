# 📘 Secure Exam Paper Management System – Backend  
Spring Boot | JWT | MySQL | Email OTP Verification

A secure backend service designed for universities to upload, assign, and control access to exam papers.  
The system ensures strict exam security through OTP-based admin verification and time-restricted paper downloads for examiners.

---

## 🚀 Core Features

### 🧑‍💼 1. Admin Registration with Email OTP
- University admin registers using email.
- System sends a **One-Time Password (OTP)** to verify identity.
- Only verified admins can access the system.
- Prevents unauthorized people from registering as admin.

### 🔐 2. Secure Authentication (JWT)
- Verified Admins and Examiners login via JWT tokens.
- Role-based access to APIs.

### 👨‍🏫 3. Admin Creates Examiner Accounts
- Only Admin is allowed to create examiner accounts.
- Admin generates username & password for the examiner.
- Examiner receives credentials (manually or via email).

### 📤 4. Admin Uploads Exam Paper
Admin provides:
- Paper Title  
- Exam Date  
- Exam Time  
- Assigned Examiner  
- PDF File  

Uploaded PDF is stored securely under `exam-pdfs/`.

### 📑 5. Admin Assigns Paper to Examiner
- Each paper is mapped to a specific examiner.
- Examiners can download **only the papers assigned to them.**

### ⏳ 6. Time-Restricted Examiner Access
Examiner can download the assigned paper **only 30 minutes before the scheduled exam time.**

Backend enforces:
```
Allow download ONLY IF
currentTime >= examStartTime - 30 minutes
```

If condition fails:
```
"Download allowed only 30 minutes before exam time."
```

### 📥 7. Secure PDF Download
- JWT authentication required.
- Examiner can only access assigned paper.
- Prevents early or unauthorized downloads.

---

## ⚙️ Tech Stack

| Component | Technology |
|----------|------------|
| Backend Framework | Spring Boot |
| Security | Spring Security + JWT |
| Email Service | JavaMail / Spring Mail |
| Database | MySQL |
| File Storage | Local Directory (`exam-pdfs/`) |
| Build Tool | Maven |

---

## 🗂 Directory Structure

```
src/
 ├── main/
 │    ├── java/com/example/exam/      # Main code
 │    ├── resources/
 │    │      ├── application.properties
 │    │      └── templates/ (if OTP emails use HTML)
 │    │
 └── test/
exam-pdfs/                             # Secure PDF storage
```

---

## 🔧 API Endpoints

### 🔹 Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register-admin` | Register admin + trigger OTP |
| POST | `/auth/verify-otp` | Verify admin OTP and activate account |
| POST | `/auth/login` | Login admin or examiner (JWT) |

---

### 🔹 Admin APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/admin/create-examiner` | Create examiner account |
| POST | `/admin/upload-paper` | Upload PDF + assign examiner + exam time |
| GET | `/admin/papers` | View all uploaded papers |

---

### 🔹 Examiner APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/examiner/assigned-papers` | View papers assigned to examiner |
| GET | `/examiner/download/{paperId}` | Download assigned paper (time-restricted) |

---

## 🛠 Configuration (application.properties)

```
spring.datasource.url=jdbc:mysql://localhost:3306/examdb
spring.datasource.username=root
spring.datasource.password=xxxx

spring.jpa.hibernate.ddl-auto=update

file.upload-dir=exam-pdfs

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email
spring.mail.password=your_app_password

jwt.secret=YOUR_SECRET_KEY
```

---

## ▶️ Running the Backend

### 1️⃣ Install dependencies
```
mvn clean install
```

### 2️⃣ Run the application
```
mvn spring-boot:run
```

Server runs at:
```
http://localhost:8080
```

---

## 🧪 Testing
Recommended tools:
- Postman  
- Angular frontend  
- Swagger (if enabled)

---

## 🌐 Deployment
(Replace your actual link)

**Live Backend URL:** https://your-backend-domain.com

---

## 📄 License
This project is for educational and learning purposes.
