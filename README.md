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

## 🚀 Quick Setup & Execution (< 5 Minutes)

### Step 1: Start Backend Server

Open Terminal and run:
```bash
cd backend
npm install
npm start
```
*(Backend server will listen on `http://localhost:3000`)*

---

### Step 2: Run Android App

#### Option A: Android Emulator (Standard for Recruiter Review - Zero Config)
1. Open the project in **Android Studio**.
2. Launch an **Android Emulator**.
3. Click **Run** (`app`).  
*(The app connects automatically via Android's built-in `10.0.2.2:3000` loopback IP).*

#### Option B: Real Physical Android Phone (USB Debugging)
If testing on a physical phone connected to your computer via USB:
1. Connect physical phone via USB with **USB Debugging** enabled.
2. In Terminal, run ADB port reverse once:
   ```bash
   adb reverse tcp:3000 tcp:3000
   ```
3. Click **Run** in Android Studio on your physical phone.

---

## 📧 How Reviewers & Recruiters Test the Email Enquiry Flow

Technical recruiters and code reviewers test the enquiry email flow using either of two standard methods:

### Method 1: Mailpit Web UI (Docker Compose)
1. Run `docker-compose up -d` from the project root.
2. Submit an enquiry from the Android application.
3. Open **`http://localhost:8025`** in any web browser.
4. You will see both the **Admin Notification Email** and **Customer Confirmation Email** delivered in real-time to the Mailpit inbox.

### Method 2: Terminal Console Log Fallback (Standalone `npm start`)
If running standalone without Docker:
1. Run `cd backend && npm start`.
2. Submit an enquiry from the Android application.
3. The backend terminal outputs the dispatched email receipt logs:
   ```text
   📧 [Admin Notification Logged] To: admin@cataloghub.com | Customer: Jane Doe (jane@example.com) | Product: Silicon Power 256GB SSD
   📧 [Customer Confirmation Logged] To: jane@example.com | Subject: Enquiry Confirmation: Silicon Power 256GB SSD
   ```

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
