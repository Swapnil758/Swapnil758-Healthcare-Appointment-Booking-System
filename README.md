🏥 Doctor Appointment System

A microservices-based Doctor Appointment System built with Spring Boot and Spring Cloud. It allows patients to search for doctors, book appointments, process payments, and scan prescriptions — all through independent, scalable services.🧩 Microservices

ServicePortDescriptioneureka_server8761Service registry and discoverydoctor_service8081Doctor management, scheduling, searchbooking_service8083Appointment booking and confirmationpatient_service8085Patient registration and managementpayment_service8086Stripe-based payment processingsecurity8087JWT-based authentication and authorizationinventory_service—Medicine inventory managementprescription_scanner—Prescription scanning and medicine extraction


✨ Features


🔐 JWT Authentication — Secure login and registration via the Security Service
👨‍⚕️ Doctor Management — Add doctors, manage schedules, time slots, and locations (State/City/Area)
🔍 Doctor Search — Search doctors by specialization, city, and area
📅 Appointment Booking — Book appointments with doctors and receive confirmation
💳 Stripe Payments — Integrated Stripe checkout for appointment payments
💊 Prescription Scanner — Scan prescriptions and extract medicine details using AI (Google Gemini)
🗄️ Medicine Inventory — Track medicine stock and availability
☁️ AWS S3 Integration — Doctor profile images stored on Amazon S3
🌐 Eureka Service Discovery — All services register and discover each other via Eureka



🛠️ Tech Stack


Java 17+
Spring Boot 3.x
Spring Cloud (Eureka, OpenFeign)
Spring Security + JWT
Spring Data JPA
MySQL
AWS S3 (doctor profile images)
Stripe API (payments)
Google Gemini AI (prescription scanning)
Maven



🚀 Getting Started

Prerequisites


Java 17 or higher
Maven 3.8+
MySQL 8.x running locally
(Optional) AWS account for S3 image uploads
(Optional) Stripe account for payment processing


1. Clone the Repository

bashgit clone https://github.com/your-username/doctor-appointment-system.git
cd doctor-appointment-system

2. Configure Databases

Each service uses its own MySQL database. Make sure MySQL is running, then update the credentials in each service's application.properties if needed (default: root / admin).

ServiceDatabasedoctor_servicedoctordbbooking_servicebookingservicedbpatient_servicepatient_dbsecuritysecuritydbnew

Databases are created automatically on first run (createDatabaseIfNotExist=true).

3. Configure API Keys

Doctor Service — add your AWS credentials in doctor_service/src/main/resources/application.properties:

propertiesaws.accessKey=YOUR_AWS_ACCESS_KEY
aws.secretKey=YOUR_AWS_SECRET_KEY
aws.region=ap-south-1
aws.s3.bucket=YOUR_BUCKET_NAME

Payment Service — add your Stripe key in payment-service/src/main/resources/application.properties:

propertiesstripe.apiKey=YOUR_STRIPE_SECRET_KEY

4. Start the Services

Start in the following order (Eureka must come first):

bash# 1. Eureka Server
cd eureka_server/eureka_server
./mvnw spring-boot:run

# 2. Security Service
cd ../../security/security
./mvnw spring-boot:run

# 3. Doctor Service
cd ../../doctor_service/doctor_service
./mvnw spring-boot:run

# 4. Patient Service
cd ../../patient_service/patient_service
./mvnw spring-boot:run

# 5. Booking Service
cd ../../booking_service/booking_service
./mvnw spring-boot:run

# 6. Payment Service
cd ../../payment-service/payment-service
./mvnw spring-boot:run

# 7. Inventory Service
cd ../../inventory-service
./mvnw spring-boot:run

# 8. Prescription Scanner
cd ../../prescription-scanner/prescription-scanner/prescription-scanner
./mvnw spring-boot:run

5. Verify Services

Once started, visit the Eureka dashboard at:

http://localhost:8761

All registered services should appear there.


📡 API Endpoints (Summary)

Security Service (:8087)

MethodEndpointDescriptionPOST/auth/registerRegister a new userPOST/auth/loginLogin and receive JWT token

Doctor Service (:8081)

MethodEndpointDescriptionPOST/doctor/addAdd a new doctorGET/doctor/allGet all doctorsPOST/searchSearch doctors by location/specialization

Booking Service (:8083)

MethodEndpointDescriptionPOST/booking/bookBook an appointmentGET/booking/allGet all bookings

Patient Service (:8085)

MethodEndpointDescriptionPOST/patient/addRegister a patientGET/patient/{id}Get patient by ID

Payment Service (:8086)

MethodEndpointDescriptionPOST/checkoutInitiate Stripe checkout for a booking

Inventory Service

MethodEndpointDescriptionGET/inventory/checkCheck medicine availabilityPOST/inventory/addAdd medicine to inventory


🗂️ Project Structure

Doctor Appointment System/
├── eureka_server/          # Service registry
├── security/               # Auth & JWT
├── doctor_service/         # Doctor management + AWS S3
├── patient_service/        # Patient management
├── booking_service/        # Appointment booking (uses Feign clients)
├── payment-service/        # Stripe payment integration
├── inventory-service/      # Medicine inventory
└── prescription-scanner/   # AI prescription scanning (Gemini)


🔒 Security


All endpoints (except /auth/**) require a valid JWT Bearer token
Tokens are issued on login and must be included in the Authorization header:


  Authorization: Bearer <your_token>


📄 License

This project is open source and available under the MIT License.


🤝 Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you'd like to change.
ShareContentDoctor appoinment Sysytem.zipzip
