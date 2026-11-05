# AI Usage Audit (AI_USAGE.md)

## 1. AI Tools & Models Used
- **AI Agent**: Cursor / Claude 3.5 Sonnet / Claude 3.7 Sonnet.
- **Role**: Software Architect, Senior Backend Engineer, Senior Android Engineer.

## 2. Prompts Provided to AI
- **Prompt 1**: High-level assignment requirements, stack definition, architectural constraints, and request for initial project design plan.
- **Prompt 2**: Approval of initial architecture plan and request to implement Phase 2 (Backend APIs & Tests).
- **Prompt 3**: Approval to execute Phase 4 & 5 (Android Clean Architecture implementation, Jetpack Compose screens, ViewModels, Hilt DI, and unit tests).

## 3. Initial Implementation Plan
- **Phase 1**: Architectural design, API contract definition, folder structure layout.
- **Phase 2 & 3**: Node.js TS backend development, Fake Store API proxy repository, Zod enquiry validation, Nodemailer SMTP abstraction, Docker Compose with Mailpit, Jest unit tests.
- **Phase 4 & 5**: Android Compose screens, MVVM ViewModels, Clean Architecture Use Cases, Retrofit client, Hilt DI, Navigation Compose.
- **Phase 6**: Unit testing suite execution, documentation (`README.md`, `DECISIONS.md`, `AI_USAGE.md`).

## 4. AI-Generated Mistakes & Manual Review/Corrections
1. **AGP 9 Compatibility**:
   - *Issue*: `com.google.dagger.hilt.android` plugin failed on AGP 9.2 (`BaseExtension not found`).
   - *Correction*: Configured Hilt via annotation processing (`ksp`) with `android.builtInKotlin=false` and updated `compileSdk = 37`.
2. **Email Validation Pattern**:
   - *Issue*: `Patterns.EMAIL_ADDRESS` caused framework mock issues in pure JVM unit tests.
   - *Correction*: Replaced with standard Kotlin Regex matching for email validation in `SubmitEnquiryUseCase`.

## 5. Actual Time Spent
- **Architecture & Design**: ~15 mins
- **Backend Development & Tests**: ~30 mins
- **Android Data, Domain & Presentation Layers**: ~40 mins
- **Android Unit Tests & Fixes**: ~15 mins
- **Documentation & Verification**: ~10 mins
- **Total Time**: ~1 hr 50 mins
