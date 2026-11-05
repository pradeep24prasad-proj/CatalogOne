# CatalogHub - Product Catalogue MVP

CatalogHub is an enquiry-based native Android product catalogue application built with Kotlin, Jetpack Compose, MVVM + Clean Architecture, and a Node.js + TypeScript Express Backend for Frontend (BFF).

---

## 🌟 Key Features

1. **Home Screen**: App header, search bar, horizontally scrollable category chips, and top 5 featured products sorted by customer rating.
2. **Category Screen**: Dynamic category loading with rating-sorted product listings.
3. **Backend Search**: Real-time keyword search against title, description, and category.
4. **Product Details Screen**: High-res image display, price, rating, category, description, and prominent "Enquire Now" action.
5. **Enquiry Form**: Validation for mandatory fields (Customer Name, Email format, Phone, Message) and SMTP email notification dispatch.
6. **Thank-You Screen**: Receipt confirmation screen following successful email delivery.

---

## 🏗 System Architecture

┌─────────────────────────────────────────────────────────────┐
│                 Android App (CatalogHub)                    │
│   Jetpack Compose ──► ViewModels ──► UseCases ──► Repo      │
└──────────────────────────────┬──────────────────────────────┘
                               │ HTTP / REST JSON (Retrofit)
                               ▼
┌─────────────────────────────────────────────────────────────┐
│              Node.js + TypeScript Express BFF                │
│   Routes ──► Controllers ──► Services ──► Repos & Providers  │
└──────────────┬──────────────────────────────┬───────────────┘
               │                              │
               ▼                              ▼
┌──────────────────────────────┐ ┌───────────────────────────┐
│     Fake Store REST API      │ │   SMTP Email Provider     │
│ (https://fakestoreapi.com)   │ │  (Nodemailer ➔ Mailpit)   │
└──────────────────────────────┘ └───────────────────────────┘

---

## 🛠 Tech Stack

- **Android**: Kotlin, Jetpack Compose, MVVM + Clean Architecture, Navigation Compose, Coroutines + StateFlow, Hilt DI, Retrofit + OkHttp, KotlinX Serialization, Coil.
- **Backend**: Node.js, TypeScript, Express.js, Zod, Nodemailer.
- **Email**: Mailpit (local SMTP mock server).
- **Testing**:
  - Backend: Jest & Supertest (13 unit/integration tests).
  - Android: JUnit 4, MockK, Coroutines Test, Turbine.

---

## 🚀 Quick Setup & Local Execution (< 10 Mins)

### Prerequisites
- Node.js (v18+)
- Docker & Docker Compose
- Android Studio / Android Emulator

### 1. Run Backend via Docker Compose (Recommended)
```bash
# From project root
docker-compose up --build -d
```
- **Backend API**: `http://localhost:3000` (from host/Postman) or `http://10.0.2.2:3000` (from Android Emulator).
- **Mailpit Web UI**: `http://localhost:8025` (View dispatched enquiry emails in real-time).

### 2. Run Backend Standalone (Alternative)
```bash
cd backend
npm install
npm run build
npm test # Executes Jest tests
npm start
```

### 3. Run Android App
1. Open project in Android Studio.
2. Ensure Android Emulator is running.
3. Click **Run** (`app`).

---

## 🧪 Running Tests

### Backend Tests
```bash
cd backend
npm test
```

### Android Unit Tests
```bash
./gradlew app:testDebugUnitTest
```

---

## 📡 API Contracts

- `GET /api/products/featured` - Returns top 5 top-rated products.
- `GET /api/categories` - Returns available product categories.
- `GET /api/categories/:category/products` - Category products sorted by rating descending.
- `GET /api/products?search=phone` - Backend keyword search.
- `GET /api/products/:id` - Single product details.
- `POST /api/enquiries` - Accepts enquiry payload, validates with Zod, dispatches notification email via SMTP.
