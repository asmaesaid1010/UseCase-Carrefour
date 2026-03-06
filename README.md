## 🩺 Cabinet Médical – Microservice de gestion des Médecins, Spécialités, Patients, Créneaux & Rendez‑vous
```plaintext
Ce projet est un microservice Spring Boot (Java 21) pour gérer :
	- Les médecins et leurs spécialités
	- Les patients
	- Les créneaux horaires (disponibilités)
	- Les rendez‑vous (réservation d’un créneau par un patient)
```

## Technologies Utilisées 
```plaintext
- Java 21
- Spring Boot
- Spring Data JPA / Hibernate
- Lombok
- PostgreSQL
- Swagger / OpenAPI 3
- Maven
- Validation Jakarta
- Git & GitHub
```

## Architecture du Projet
```plaintext
src/
 └── main/
     └── java/
         └── com.asmae.cabinetmedical
            ├── config               # GlobalExceptionHandler, Swagger.
            ├── web                  # REST Controllers
            ├── service              # Interfaces service
            |   └── impl             # Logique métier + @Transactional
            ├── repository           # Spring Data JPA
            ├── model                # Entités JPA
            |    └── enums           # Enums (statut RDV...)
            ├── dto                  # DTOs + validations
            ├── validation           # Groupes (OnCreate, OnUpdate)
            ├── mapper               # MapStruct mappers
            ├── exception            # Exceptions personnalisées
            ├── utils                # Constantes (messages, logs)
            └── CabinetMedicalServiceApplication.java  # Classe principale
    └── resources/
         └── application.yml
```
		 
## Démarrer le Projet 
```plaintext
Lancer l’application : mvn spring-boot:run

Application disponible sur : 👉 http://localhost:8081

Swagger UI : 👉 http://localhost:8081/swagger-ui.html 
```

## Endpoints Cabinet Médical

### Endpoints Médecins
```plaintext
▶️ Obtenir un médecin

**GET** `/api/v1/medecins/{id}`  

▶️ Mettre à jour un médecin  

**PUT** `/api/v1/medecins/{id}`  

▶️ Supprimer un médecin  

**DELETE** `/api/v1/medecins/{id}`

▶️ Lister les médecins  

**GET** `/api/v1/medecins`  

▶️ Ajouter un médecin à une spécialité  

**POST** `/api/v1/medecins/specialites/{specialiteId}`  
```
### Endpoints Spécialités
```plaintext
▶️ Obtenir une spécialité par ID

**GET** `/api/v1/specialites/{id}`  

▶️ Modifier une spécialité  

**PUT** `/api/v1/specialites/{id}`  

▶️ Supprimer une spécialité  

**DELETE** `/api/v1/specialites/{id}`  

▶️ Liste paginée des spécialités  

**GET** `/api/v1/specialites`  

▶️ Créer une spécialité  

**POST** `/api/v1/specialites`  

▶️ Rechercher une spécialité par code  

**GET** `/api/v1/specialites/code/{codeName}`  
```
### Endpoints Patients
```plaintext
▶️ Obtenir un patient par ID

**GET** `/api/v1/patients/{id}`  

▶️ Mettre à jour un patient  

**PUT** `/api/v1/patients/{id}`  

▶️ Supprimer un patient  

**DELETE** `/api/v1/patients/{id}`  

▶️ Liste paginée des patients  

**GET** `/api/v1/patients`  

▶️ Créer un patient  

**POST** `/api/v1/patients`  

▶️ Rechercher un patient par email  

**GET** `/api/v1/patients/email/{email}`  
```
### Endpoints Créneaux (Disponibilités)
```plaintext
▶️ Obtenir un créneau

**GET** `/api/v1/creneaux/{id}`  

▶️ Mettre à jour un créneau  

**PUT** `/api/v1/creneaux/{id}`  

▶️ Supprimer un créneau  

**DELETE** `/api/v1/creneaux/{id}`  

▶️ Créer un créneau pour un médecin  

**POST** `/api/v1/creneaux/medecins/{medecinId}`  

▶️ Désactiver un créneau  

**PATCH** `/api/v1/creneaux/{id}/desactiver`  

▶️ Activer un créneau  

**PATCH** `/api/v1/creneaux/{id}/activer`  

▶️ Lister tous les créneaux  

**GET** `/api/v1/creneaux`  

▶️ Lister les créneaux d’un médecin  

**GET** `/api/v1/creneaux/by-medecin/{medecinId}`
```
### Endpoints Rendez-vous
```plaintext
▶️ Obtenir un rendez-vous

**GET** `/api/v1/rendezvous/{id}`  

▶️ Mettre à jour un rendez-vous  

**PUT** `/api/v1/rendezvous/{id}`  

▶️ Supprimer un rendez-vous 

**DELETE** `/api/v1/rendezvous/{id}`  

▶️ Réserver un créneau  

**POST** `/api/v1/rendezvous/reserver/patients/{patientId}/creneaux/{creneauId}`  

▶️ Historiser en NON_PRESENT  

**PATCH** `/api/v1/rendezvous/{id}/non-present`  

▶️ Historiser en HONORE  

**PATCH** `/api/v1/rendezvous/{id}/honorer`  

▶️ Annuler un rendez-vous  

**PATCH** `/api/v1/rendezvous/{id}/annuler`  

▶️ Lister tous les rendez-vous

**GET** `/api/v1/rendezvous`
```


