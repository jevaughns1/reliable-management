# Reliable Inventory Management System (RIMS)

## 🌟 Overview

The Reliable Inventory Management System (RIMS) is a full-stack warehouse inventory and product tracking application designed for enterprise use. It leverages a modern cloud-native architecture for high availability and scalability.

This project is built using:
* **React + AWS Amplify** for a fast, easily deployable frontend.
* **Spring Boot + PostgreSQL** hosted on **AWS EC2** for the secure backend API.

## 📦 Key Features

* **Warehouse Management:** Create, view, update, and delete warehouse entities, including tracking of maximum storage capacity.
* **Product Catalog:** Maintain a centralized product list (SKU, unit, price).
* **Inventory Control:** Manage specific stock levels (Quantity, Storage Location, Expiration Date) within each warehouse.
* **Expiration Alerts:** Dedicated dashboard for critical inventory alerts:
    * **Expired Stock**
    * **Nearing Expiration** (within a defined window).
* **User Experience:** Modern, responsive UI built with React-Bootstrap.

## ☁️ Architecture 

| Component | Technology | Hosting Environment | Description |
| :--- | :--- | :--- | :--- |
| **Frontend** | React, `react-router-dom` | **AWS Amplify** (Static Hosting) | Provides the main user interface and handles all routing and state management. |
| **Backend API** | Spring Boot (Java), RESTful | **AWS EC2** Instance | Handles business logic, security, and serves as the intermediary to the database. |
| **Database** | PostgreSQL | (e.g., EC2 or AWS RDS) | Persistent storage for all warehouse, product, and inventory data. |

## 🛠️ Tech Stack

### Frontend (React & AWS Amplify)
* **Framework:** React 18+
* **Styling:** React-Bootstrap
* **Cloud Config:** AWS Amplify (used for deployment and configuring API endpoint).

### Backend (Spring Boot & PostgreSQL)
* **Language/Framework:** Java, Spring Boot
* **Database:** **PostgreSQL**
* **Deployment:** AWS EC2 Instance
* **Status:** Ready for implementing global **Logging** and **AOP** features.

## 🚀 Getting Started

### Prerequisites

* Node.js (LTS version)
* Java Development Kit (JDK 17+)
* Maven or Gradle (for Spring Boot build)
* A running **PostgreSQL** instance.
* AWS CLI and a configured AWS account (for deployment).

### 1. Backend Setup & EC2 Deployment (Spring Boot)

1.  **Clone the repository:**
    ```bash
    git clone [YOUR_REPO_URL]
    cd reliable-inventory-system/backend # (Adjust path if needed)
    ```
2.  **Configure PostgreSQL:** Update your application properties file (`src/main/resources/application.properties` or `application.yml`) with the necessary connection details for your PostgreSQL instance.
3.  **Build the Executable JAR:**
    ```bash
    ./mvnw clean package
    ```
4.  **Deploy to EC2:** Transfer the resulting `.jar` file from the `target` directory to your EC2 instance and run it:
    ```bash
    # Example commands after SSH into EC2 instance
    java -jar reliable-inventory-system.jar
    # (Ensure your EC2 Security Group allows traffic on the required API port, e.g., 8080)
    ```

### 2. Frontend Setup & Amplify Deployment (React)

1.  **Navigate to the frontend directory:**
    ```bash
    cd frontend # (Adjust path if needed)
    ```
2.  **Update API Endpoint:** Your frontend needs the public IP/DNS of your running EC2 instance. Update the API endpoint URL inside your `aws-exports.js` file (or other environment variables) to point to the EC2 API.

    * **Crucial Step:** This ensures your Amplify-hosted frontend can talk to your EC2-hosted backend.

3.  **Install dependencies:**
    ```bash
    npm install
    # or
    yarn install
    ```
4.  **Local Testing:**
    ```bash
    npm start
    ```
5.  **Amplify Deployment:** Use the Amplify CLI to connect this project to your AWS account for continuous static hosting.
    ```bash
    amplify init
    amplify add hosting # Follow prompts for static web hosting
    amplify publish
    ```

