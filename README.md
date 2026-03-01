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
- Spring Security (inkl. OAuth2 Client)
- Spring HATEOAS
- JWT
- PostgreSQL
- JPA / Hibernate

## Autentisering
### OAuth2 (GitHub) - REKOMMENDERAT
1. Gå till `/oauth2/authorization/github`
2. Logga in via GitHub.
3. Efter lyckad inloggning skapas en användare automatiskt i systemet.

### JWT
1. Logga in via `/api/auth/login`
2. Backend returnerar JWT
3. Skicka token i header:
```
Authorization: Bearer <TOKEN>
```

---

## HATEOAS
API:et använder hypermedia (HATEOAS). Svar från mappar och filer innehåller nu länkar till relaterade resurser.
Exempel på svar för en mapp:
```json
{
  "id": 1,
  "name": "Skola",
  "_links": {
    "self": { "href": "http://localhost:8080/api/folders" },
    "all-folders": { "href": "http://localhost:8080/api/folders" }
  }
}
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
- file → välj fil
- folderId → 1

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

## Konfiguration av GitHub OAuth2
För att OAuth2-inloggning ska fungera måste du skapa en **OAuth App** på GitHub:
1. Gå till **Settings** -> **Developer settings** -> **OAuth Apps** -> **New OAuth App**.
2. Sätt `Homepage URL` till `http://localhost:8080`.
3. Sätt `Authorization callback URL` till `http://localhost:8080/login/oauth2/code/github`.
4. Kopiera `Client ID` och `Client Secret` till din `application.properties` eller sätt dem som miljövariabler (`GITHUB_CLIENT_ID` och `GITHUB_CLIENT_SECRET`).

---

## Sammanfattning
Projektet visar ett komplett REST-API med JWT-säkerhet, filhantering och databasstöd.
