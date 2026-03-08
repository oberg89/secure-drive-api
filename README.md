# Secure Drive API

Secure Drive API är ett backend-API byggt med **Spring Boot** som fungerar som ett säkert filsystem där användare kan skapa mappar och lagra filer.

API:t implementerar **REST-principer, HATEOAS, JWT-autentisering och GitHub OAuth2**.

---

# Funktioner

* Registrera användare
* Logga in och få JWT-token
* Logga in via GitHub (OAuth2 / OpenID Connect)
* Skapa mappar
* Ladda upp filer till mappar
* Lista mappar och filer
* Ladda ner filer
* Ta bort filer

Alla mappar och filer är kopplade till en användare.

---

# Tekniker

* Java 21+
* Spring Boot
* Spring Security
* Spring OAuth2 Client
* Spring HATEOAS
* JWT (JSON Web Token)
* PostgreSQL
* Spring Data JPA / Hibernate
* Swagger / OpenAPI

---

# API Dokumentation

Swagger UI finns på:

http://localhost:8080/swagger-ui.html

Här kan alla endpoints testas direkt i webbläsaren.

---

# Autentisering

API:t stödjer två typer av autentisering.

---

# GitHub OAuth2 (rekommenderat)

1. Gå till:

/oauth2/authorization/github

2. Logga in med ditt GitHub-konto.

3. Efter lyckad inloggning skapas en användare automatiskt i systemet.

4. Backend genererar en **JWT-token** och redirectar till Swagger UI.

Exempel:

http://localhost:8080/swagger-ui/index.html?token=JWT_TOKEN

---

# Viktigt om GitHub-token

Token som genereras efter GitHub-login kan användas i externa API-klienter som:

- Bruno
- Postman
- curl

Exempel:

Authorization: Bearer <TOKEN>

Denna token fungerar för API-anrop utanför Swagger.

---

# Swagger UI

Swagger använder **inte automatiskt token från URL:en**.

För att testa endpoints i Swagger gör man istället:

1. Registrera en användare

POST /api/auth/register

```
{
  "username": "user",
  "password": "password"
}
```

2. Logga in

POST /api/auth/login

```
{
  "username": "user",
  "password": "password"
}
```

Svar:

```
{
  "token": "JWT_TOKEN"
}
```

3. Klicka på **Authorize** i Swagger.

4. Ange:

Authorization: Bearer <TOKEN>

Efter detta kan alla endpoints testas direkt i Swagger.

---

# Mappar

## Skapa mapp

POST /api/folders

Header:

Authorization: Bearer <TOKEN>

Body:

```
{
  "name": "Skola"
}
```

---

## Lista mappar

GET /api/folders

---

# Filer

## Ladda upp fil

POST /api/files/upload

Header:

Authorization: Bearer <TOKEN>

Body (multipart/form-data):

file → fil att ladda upp  
folderId → ID på mappen

---

## Lista filer

GET /api/files

---

## Ladda ner fil

GET /api/files/{id}

---

## Ta bort fil

DELETE /api/files/{id}

---

# HATEOAS

API:t använder **Spring HATEOAS** och returnerar HAL-JSON.

Exempel:

```
{
  "id": 1,
  "name": "Skola",
  "_links": {
    "all-folders": {
      "href": "http://localhost:8080/api/folders"
    }
  }
}
```

---

# Säkerhet

API:t är stateless och använder JWT.

* Ingen server-session
* CSRF avstängt
* Alla endpoints kräver autentisering utom auth-endpoints

Användare kan **endast komma åt sina egna filer och mappar**.

---

# Starta projektet

Starta servern:

./gradlew bootRun

Servern kör på:

http://localhost:8080

---

# GitHub OAuth2 konfiguration

För att GitHub-inloggning ska fungera:

1. Skapa en OAuth App på GitHub

2. Sätt:

Homepage URL  
http://localhost:8080

Authorization callback URL  
http://localhost:8080/login/oauth2/code/github

3. Lägg in `client-id` och `client-secret` i `application.properties`.

---

# Projektstruktur

controller  
service  
repository  
model  
dto  
security

Arkitekturen följer en **layered architecture**.

---

# Sammanfattning

Projektet implementerar ett komplett REST-API med:

* JWT autentisering
* GitHub OAuth2 login
* HATEOAS
* filhantering
* PostgreSQL databas
* Swagger API-dokumentation