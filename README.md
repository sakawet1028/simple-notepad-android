# Simple Notepad (Android)

Beginner-friendly offline notepad app.

**Package:** `com.sakawet.simplenotepad`  
**Stack:** Kotlin, Jetpack Compose, Material 3, Navigation Compose  
**minSdk:** 26 · **Java:** 17 · **No internet required**

## Mastery Steps

1. **Foundation & Project Setup** (this commit) — project, theme, two screens, navigation.
2. **Notepad Core Features** — Room, create / read / edit / delete notes.
3. **UI/UX & Testing** — polish empty states and basic tests.
4. **Build, GitHub Workflow & Mobile Installation** — APK and install on a phone.

## Open the project

1. Install the latest stable [Android Studio](https://developer.android.com/studio).
2. **File → Open** this folder.
3. Let Gradle sync finish.
4. Pick an emulator (API 34+) or a USB device, then Run.

Android Studio will generate the Gradle Wrapper if `gradlew` is missing.

## What Step 1 can do

- Launch the Notes screen (empty state).
- Tap the FAB to open Note Edit.
- Type a title and content (not saved yet).
- Press Back to return to Notes.

Notes are **not** stored on disk until Step 2.

## What we intentionally skipped (for now)

Room, ViewModel logic, Repository, Hilt, Retrofit, Firebase, internet permission.
