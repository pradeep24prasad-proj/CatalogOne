# CatalogHub - Product Catalogue MVP

CatalogHub is an enquiry-based native Android product catalogue application built with Kotlin, Jetpack Compose, MVVM + Clean Architecture, and a Node.js + TypeScript Express Backend for Frontend (BFF).

---

## 🌟 Key Features

1. **Home Screen**: App header, search bar, horizontally scrollable category chips, top-rated featured products grid, and "Browse All" navigation.
2. **Category Screen**: Dynamic category loading with 2-column rating-sorted product grid.
3. **Backend Search**: Real-time keyword search against title, description, and category.
4. **Product Details Screen**: High-res image display, price, rating, category, description, and prominent "Enquire Now" action.
5. **Enquiry Form**: Form validation (Customer Name, Email format, Phone, Message) and SMTP email notification dispatch.
6. **Thank-You Screen**: Confirmation screen following successful enquiry submission.

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

- **Android**: Kotlin, Jetpack Compose, ViewBinding, MVVM + Clean Architecture, Navigation Compose, Coroutines + StateFlow, Hilt DI, Retrofit + OkHttp, KotlinX Serialization, Coil.
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
- Android Studio / Android Emulator / Physical Android Device

### 1. Run Backend via Docker Compose (Recommended)
```bash
# From project root
docker-compose up --build -d
```
- **Backend API**: `http://localhost:3000` (from host/Postman) or `http://10.0.2.2:3000` / `http://127.0.0.1:3000` (from Android Emulator / Device).
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

#### Option A: Running on Android Emulator (Standard for Recruiter Review)
1. Open the project in Android Studio.
2. Launch an **Android Emulator**.
3. Click **Run** (`app`).

#### Option B: Running on a Physical Android Device (USB Debugging)
When testing on a real physical Android device connected via USB:
1. Enable **USB Debugging** on the device.
2. Forward local backend port 3000 to the connected device via ADB:
   ```bash
   adb reverse tcp:3000 tcp:3000
   ```
3. Run the app from Android Studio on your physical device.

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

- `GET /api/products` - Returns all products.
- `GET /api/products/featured` - Returns top 5 top-rated products.
- `GET /api/categories` - Returns available product categories.
- `GET /api/categories/:category/products` - Category products sorted by rating descending.
- `GET /api/products?search=phone` - Backend keyword search.
- `GET /api/products/:id` - Single product details.
- `POST /api/enquiries` - Accepts enquiry payload, validates with Zod, dispatches notification email via SMTP.
