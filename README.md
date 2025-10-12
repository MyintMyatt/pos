# POS (Point Of Sale)

## Features

- Admin Section
- User Management
- Menu Management
- Inventory Management
- Report

## Tech Stack 

- Java 17, Spring Boot 3.5.6
- Spring Vault
- Redis
- PostgreSQL
- Docker

## Clone Repo
```bash
  git clone https://github.com/MyintMyatt/pos-backend.git
```
1. **Change project dir**
```bash
  cd pos-backend
```
2. **Run docker compose**
```bash
   docker compose up -d
```
3. **Open browser**
```bash
   http://localhost:8200/
```
4. **Enter Vault Token**
```bash
   root
```
5. **create secret and secret path is**
```bash
   pos-secret
```
6. **secret data**
```bash
   {
      "db-password": "your db password",
      "db-url": "your db url",
      "db-username": "your db username",
      "jwt-secret-key": "0b1ac160f2abb47ca5a5de8cfe139586b55db5cc6974837979d42366a67b9e89aceae2639005059e277243c81239f8be299f453c4d8e36a21a8fe3e36df1e90b",
      "frontend-url": "http://localhost:5173" <== this url is for CORS
    }
```
7. **Run Spring boot application**   
