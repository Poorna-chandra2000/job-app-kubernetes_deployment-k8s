# Job-App-Microservces-project
Job Application Management System
This application manages job listings, company profiles, and user authentication, with a microservices architecture leveraging Spring Boot, Spring Cloud, and OpenFeign for communication. Configuration management and service discovery are handled by Spring Cloud Config Server and Eureka.
# Microservices Application with Kafka Event Streaming and Docker

This project demonstrates a microservices architecture using Spring Boot, Kafka for event streaming, and Docker for containerization. It includes various services like job management, company management, review management, and user authentication, along with real-time notification updates through Kafka.

## Project Structure

### Microservices
1. **Discovery Service** - Manages service registration and discovery.
2. **Config Server** - Manages configuration properties across services.
3. **API Gateway** - Serves as a central gateway with JWT-based authentication.
4. **Job Management Service (jobms)** - Manages job-related data.
5. **Company Management Service (companyms)** - Manages company-related data.
6. **User Service (userms)** - Handles user authentication and management with JWT.
7. **Review Management Service (reviewms)** - Produces Kafka events for real-time review updates.
8. **Notification Service (notifyms)** - Consumes Kafka events to update and calculate average ratings.

### Databases
Each microservice is backed by a PostgreSQL database container:
- **job-db**, **company-db**, **notify-db**, **review-db**, and **user-db**

### Event Streaming with Kafka
- **reviewms** acts as a producer, sending events to Kafka.
- **notifyms** acts as a consumer, processing these events to calculate and update ratings in real-time.

### Monitoring and Tracing
1. **Zipkin** - For distributed tracing.
2. **Kafka UI** - For monitoring Kafka topics and messages.

## Prerequisites
- Docker
- Docker Compose

## Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/yourproject.git
   cd yourproject

Project Structure
Microservices
Config Server

Manages centralized configuration.
Git repository for configurations: GitHub Repository
Runs on port 8888.
Company Service

Manages CRUD operations for company profiles.
REST endpoints for creating, updating, deleting, and retrieving company details.
Job Service

Manages CRUD operations for job postings.
Integrates with the Company Service to fetch company details and with the Review Service to get reviews.
Utilizes OpenFeign for inter-service communication.
Review Service

Provides a review management service, enabling retrieval of reviews for companies.
User Service

Handles user authentication and authorization.
Supports JWT-based login and sign-up functionalities.
Configuration & Dependencies
Spring Cloud Config Server
## application.yml for Config Server:
# Job-App-Microservices-Project

## Spring Cloud Config Server Configuration

```yaml
spring:
  application:
    name: configservergit
  cloud:
    config:
      server:
        git:
          uri: https://github.com/Poorna-chandra2000/jobaapp-config-server.git
          username: [YOUR_GITHUB_USERNAME]
          password: [YOUR_GITHUB_PAT]
        default-label: main
server:
  port: 8888
eureka:
  client:
    serviceUrl:
      defaultZone: http://discovery-service:8761/eureka
    register-with-eureka: true
    fetch-registry: true

OpenFeign Client (in Job Service)
Interface for Review Service communication:
java
Copy code
@FeignClient(name = "reviewms", url = "http://reviewms:8083")
public interface ReviewClient {
@GetMapping("/reviews")
List<Review> getReviews(@RequestParam("companyId") Long companyId);
}
REST API Endpoints
Company Service
Method	Endpoint	Description
GET	/companies	Get all companies
GET	/companies/{id}	Get a specific company by ID
POST	/companies	Add a new company
PUT	/companies/{id}	Update company details
DELETE	/companies/{id}	Delete a specific company by ID
PUT	/companies/rating/{id}/{rating}	Update company rating
Job Service
Method	Endpoint	Description
GET	/jobs	Get all job listings
GET	/jobs/{id}	Get a specific job by ID 
POST	/jobs	Create a new job
PUT	/jobs/{id}	Update a specific job
DELETE	/jobs/{id}	Delete a specific job
GET	/jobs/withcompany	Get all jobs with associated company
GET	/jobs/jobwithcompany/{id}	Get job by ID with associated company
Auth Service
Method	Endpoint	Description
POST	/auth/signup	Register a new user
POST	/auth/login	Authenticate a user
GET	/auth/{id}	Get user details by ID

version: "3.8"
services:
zipkin:
image: openzipkin/zipkin
container_name: zipkin
ports:
- "9411:9411"
networks:
- job-network

discovery-service:
image: poorna2601/service-reg
container_name: discovery-service
networks:
- job-network
ports:
- "8761:8761"

config-server:
image: poorna2601/configservergit
container_name: config-server
networks:
- job-network
depends_on:
- discovery-service

api-gateway:
image: poorna2601/api-gateway
container_name: api-gateway
environment:
- JWT_KEY=asdfladf98a7df89aysf8d9yasiudfha9s87dfa89syudhfa98sdfyaisuhdfa98sfy
networks:
- job-network
depends_on:
- jobms
- companyms
- reviewms
- user
- config-server
- zipkin
ports:
- "8080:8080"

# Database containers for each service
job-db, company-db, notify-db, review-db, user-db:
image: postgres
environment:
- POSTGRES_DB=<service-specific-db>
- POSTGRES_USER=user
- POSTGRES_PASSWORD=password
networks:
- job-network
volumes:
- <service>-db-data:/var/lib/postgresql/data

# Microservices containers
notifyms, jobms, companyms, reviewms, userms:
image: poorna2601/<microservice-name>
container_name: <microservice-name>
networks:
- job-network
depends_on:
- config-server
- zipkin
- <service>-db

# Kafka and Kafka UI
kafka:
image: docker.io/bitnami/kafka:3.8
container_name: kafka
volumes:
- kafka_data:/bitnami
networks:
- job-network
environment:
# KRaft settings
KAFKA_CFG_NODE_ID: 0
KAFKA_CFG_PROCESS_ROLES: controller,broker
KAFKA_CFG_CONTROLLER_QUORUM_VOTERS: 0@kafka:9093
# Listeners
KAFKA_CFG_LISTENERS: PLAINTEXT://:9092,CONTROLLER://:9093
KAFKA_CFG_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092
KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP: CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT
KAFKA_CFG_CONTROLLER_LISTENER_NAMES: CONTROLLER
KAFKA_CFG_INTER_BROKER_LISTENER_NAME: PLAINTEXT

kafka-ui:
container_name: kafka-ui
image: ghcr.io/kafbat/kafka-ui:latest
ports:
- "8090:8080"
depends_on:
- kafka
environment:
KAFKA_CLUSTERS_0_NAME: local
KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: kafka:9092

volumes:
job-db-data:
company-db-data:
review-db-data:
user-db-data:
kafka_data:
notify-db-data:

networks:
job-network:
