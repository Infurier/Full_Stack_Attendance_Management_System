# Full Stack Attendance Management System

A complete Full Stack Attendance Management System featuring a Spring Boot backend and a React frontend. The application allows tracking and managing attendance for students and teachers, with role-based access and a modern UI.

## Features
- **Frontend**: Built with React, Material-UI (MUI), and Recharts for a clean, responsive, and interactive user interface.
- **Backend**: Built with Java Spring Boot, utilizing Spring Security (JWT) for authentication and Spring Data JPA for database operations.
- **Database**: MySQL integration for robust and scalable data storage.
- **Roles**: Distinct panels for Admin, Teacher, and Student roles.

---

## Prerequisites

Before running the application, ensure you have the following installed on your system:

### For Windows, macOS, and Linux
1. **Java Development Kit (JDK) 17 or higher**
   - [Download Java](https://www.oracle.com/java/technologies/javase-downloads.html)
   - Verify installation: `java -version`
2. **Node.js (v14 or higher) and npm**
   - [Download Node.js](https://nodejs.org/)
   - Verify installation: `node -v` and `npm -v`
3. **MySQL Server (v8.0 or higher)**
   - [Download MySQL](https://dev.mysql.com/downloads/mysql/)
   - Make sure the MySQL service is running.

---

## 🛠️ Installation & Setup

### 1. Database Setup (MySQL)

By default, the application is configured to connect to a local MySQL instance with the following credentials:
- **Username**: `root`
- **Password**: `root`

1. Open your MySQL client (e.g., MySQL Workbench or CLI).
2. Create the database (the application can also auto-create it if configured, but it's best to create it manually):
   ```sql
   CREATE DATABASE attendance_db;
   ```
*(Note: If you have a different MySQL username or password, update the `application.properties` file located at `attendance-server/src/main/resources/application.properties`)*

### 2. Backend Setup (Spring Boot)

The backend uses Maven wrapper (`mvnw`), so you don't need to install Maven globally.

1. Open a terminal and navigate to the backend directory:
   ```bash
   cd attendance-server
   ```
2. Run the Spring Boot application:

   - **On Windows:**
     ```cmd
     mvnw.cmd spring-boot:run
     ```
   - **On macOS / Linux:**
     ```bash
     ./mvnw spring-boot:run
     ```
     *(If you get a permission denied error on Mac/Linux, run `chmod +x mvnw` first)*

The backend server will start running at **http://localhost:8080**.

### 3. Frontend Setup (React)

1. Open a **new terminal window/tab** and navigate to the frontend directory:
   ```bash
   cd attendance-client
   ```
2. Install the necessary dependencies:
   ```bash
   npm install
   ```
3. Start the React development server:
   ```bash
   npm start
   ```

The frontend application will start running and should automatically open in your default browser at **http://localhost:3000**.

---

## 💻 Operating System Specific Notes

### Windows
- Use Command Prompt (CMD) or PowerShell.
- Use `mvnw.cmd` to run the Spring Boot server.
- Ensure MySQL is added to your system's Environment Variables if you wish to run `mysql` commands from the terminal.

### macOS & Linux
- Use your preferred Terminal (bash, zsh, etc.).
- Use `./mvnw` to run the Spring Boot server.
- You might need to grant execution permissions to the Maven wrapper script: `chmod +x mvnw`.
- For MySQL on Linux, you can manage the service using `sudo systemctl start mysql`.
- For MySQL on macOS (Homebrew), use `brew services start mysql`.

---

## Project Structure

```text
Full_Stack_Attendance_Management_System/
├── attendance-client/        # React JS Frontend Application
│   ├── public/               # Static public assets
│   ├── src/                  # React components and pages
│   └── package.json          # Node.js dependencies
│
├── attendance-server/        # Spring Boot Java Backend
│   ├── src/main/java/        # Java source code (Controllers, Models, Services)
│   ├── src/main/resources/   # App configuration (application.properties)
│   └── pom.xml               # Maven dependencies
│
├── .gitignore                # Git ignore rules
└── LICENSE                   # MIT License file
```

## License

This project is licensed under the [MIT License](LICENSE).
