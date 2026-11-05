# Architecture Decisions & Trade-Offs (DECISIONS.md)

## 1. Why Kotlin and Jetpack Compose?
- **Declarative UI**: Jetpack Compose reduces boilerplate code compared to traditional XML layouts and provides reactive UI updates via `StateFlow`.
- **Clean Architecture Integration**: Unidirectional Data Flow (UDF) pairs cleanly with ViewModel state exposure (`UiState`).

## 2. Why Node.js, TypeScript, and Express?
- **Lightweight BFF (Backend for Frontend)**: Node.js offers rapid asynchronous request processing ideal for proxying external APIs.
- **Type Safety & Validation**: TypeScript ensures interface parity across backend layers, while Zod provides runtime schema validation for incoming JSON payloads.

## 3. Why Fake Store API Abstraction?
- **Constraint Compliance**: Android client must never directly hit external APIs like Fake Store API.
- **Server-Side Enrichment**: Fake Store API lacks native rating-based sorting and complex multi-field search. By routing through the Node.js backend, these business rules (top 5 sorting, substring search) are executed centrally.

## 4. How Featured Products Are Selected
- Fake Store API provides catalog items with `rating: { rate: number, count: number }`.
- The backend fetches product catalog items, sorts descending by `rating.rate`, and returns the top 5 items.

## 5. Email Provider Abstraction & Testing Strategy
- Designed interface `IEmailProvider` in backend services.
- `SmtpEmailProvider` implements Nodemailer to send emails.
- Used **Mailpit** in Docker Compose to capture all outgoing emails without requiring real external SMTP credentials or exposing secrets.

## 6. Future Improvements
- **Caching Layer**: Introduce Redis/in-memory cache with TTL in Node.js backend to minimize external latency to Fake Store API.
- **Pagination**: Add offset/limit pagination on category and search listings.
- **UI Enhancements**: Add shimmer placeholders during image loading.
