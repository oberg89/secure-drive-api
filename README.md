# Secure Drive API

Secure Drive API är ett backend-API byggt i **Spring Boot** som fungerar som ett säkert filsystem.

## Funktioner
- Registrera användare
- Logga in och få JWT-token
- Skapa mappar
- Ladda upp filer till mappar
- Lista mappar och filer
- Ladda ner filer
- Ta bort filer

## Tekniker
- Java 25
- Spring Boot 4
- Spring Security
- JWT
- PostgreSQL
- JPA / Hibernate
- Bruno

## Autentisering (JWT)
1. Logga in via `/api/auth/login`
2. Backend returnerar JWT
3. Skicka token i header:
```
Authorization: Bearer <TOKEN>
```

## Starta projektet
```
./gradlew bootRun
```

API körs på:
http://localhost:8080

---

## Bruno – API-kommandon

### Registrera användare
POST /api/auth/register
```json
{
  "username": "användarnamn",
  "password": "Lösenord"
}
```

### Logga in
POST /api/auth/login
```json
{
  "username": "användarnamn",
  "password": "Lösenord"
}
```

Svar:
```json
{ "token": "JWT_TOKEN" }
```

---

### Skapa mapp
POST /api/folders  
Header:
```
Authorization: Bearer <TOKEN>
```

Body:
```json
{ "name": "Skola" }
```

---

### Visa mappar
GET /api/folders  
Header:
```
Authorization: Bearer <TOKEN>
```

---

### Ladda upp fil
POST /api/files/upload  
Header:
```
Authorization: Bearer <TOKEN>
```

Body (Multipart Form):
- file ? välj fil
- folderId ? 1

---

### Visa filer
GET /api/files  
Header:
```
Authorization: Bearer <TOKEN>
```

---

### Ladda ner fil
GET /api/files/{id}  
Header:
```
Authorization: Bearer <TOKEN>
```

---

### Ta bort fil
DELETE /api/files/{id}  
Header:
```
Authorization: Bearer <TOKEN>
```

---

## Säkerhet
- Stateless (JWT)
- Ingen session
- CSRF av
- Endast /api/auth/** är publikt

---

## Sammanfattning
Projektet visar ett komplett REST-API med JWT-säkerhet, filhantering och databasstöd.
