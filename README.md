# ReturnPoint Ireland

A personal Android directory of Irish deposit return points: the shops and
machines where you can return cans and bottles. You keep your own list of
places, record how each one takes returns, and open any of them in a maps app.

Mobile App Development 1, Assignment 1, SETU.

## Features

- Add a return point with name, address, town, county and return method
  (reverse vending machine, manual at the counter, or unknown), plus notes.
- County and return method are chosen from dropdown lists. Required fields are
  checked before saving.
- List all return points in a RecyclerView, with a message when the list is empty.
- Edit a return point, or delete it after a confirmation dialog.
- Search by name, address or town (not case sensitive).
- Filter by county and by return method. Search and filters work together, and
  Reset clears them.
- "Map" button on each row opens the address in a maps app using an implicit
  `geo:` intent. If no maps app is installed, a message is shown.
- Toolbar menu navigation: Add and View list from the main screen, Add from the list.

## Screens

| Screen | Class |
|---|---|
| Main (menu) | `MainActivity` |
| List, search, filters | `ReturnPointListActivity` + `ReturnPointAdapter` |
| Add / Edit form | `AddEditActivity` |

Data lives in `ReturnPointStore`, shared through the `AppData` object.

## Data

Storage is in memory only, so changes are lost when the app is closed.
Three demo records, labelled "Demo", are added at start-up so the list is not empty.

## Running the app

1. Open the project in Android Studio.
2. Let Gradle sync.
3. Run the `app` configuration on an emulator or device (Android 7.0, API 24 or later).

Built with Kotlin and XML layouts. Libraries: AndroidX AppCompat, Core KTX,
Activity, ConstraintLayout, RecyclerView and Material Components.

## Git

- Each feature was built on its own `feature/tNN-<name>` branch and merged into
  `main` with a merge commit.
- `v0.1-crud` tags the add, list, edit and delete milestone.

## References

- SETU Mobile App Development 1 (2026), https://tutors.dev/course/mobile-app-dev-1-2026-setu
  - Topic 4 lab: Building a Simple Android CRUD App for PlacedMark (starting structure: store, main screen, add/edit screen).
  - Topic 5 lab: Refactoring the app with adapter (menu, list screen, RecyclerView adapter, edit and delete).
  - Topic 6 lab: Refactoring the RecyclerView (delete confirmation dialog).
- Android Developers, Common intents: Maps, https://developer.android.com/guide/components/intents-common#Maps
- Material Components for Android, https://m3.material.io/develop/android/mdc-android
