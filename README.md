# iTunes Code Challenge

An Android app that searches the Apple iTunes catalogue, lists songs, plays their
previews, and browses albums — built as a take‑home challenge to demonstrate modern
Android architecture: a multi‑module codebase, MVVM, Jetpack Compose, Hilt, Room
offline‑first caching, Paging 3, coroutines/Flow, and a replaceable network layer,
all covered by unit and UI tests.

## Architecture

```
:app                      Hilt graph, MainActivity, NavHost, splash
│
├─ :feature:songs         Home: search + recently played
├─ :feature:player        Now‑playing screen + audio controls
├─ :feature:album         Album detail / track list
│
├─ :core:domain           Pure Kotlin: models, repository interfaces, use cases
├─ :core:data             Repository impl, Paging RemoteMediator, mappers
├─ :core:network          Retrofit/OkHttp behind ItunesNetworkDataSource (swap boundary)
├─ :core:database         Room: SongEntity, DAO, ItunesDatabase (single source of truth)
├─ :core:player           Media3/ExoPlayer impl of the AudioPlayer interface
├─ :core:designsystem     Theme + reusable Compose components (Artwork, MediaRow, …)
├─ :core:common           Result wrapper, dispatcher provider
└─ :core:testing          Fakes (FakeSongRepository, FakeAudioPlayer) + test rules
```

### Key design decisions

- **Dependency inversion at every boundary.** `:core:domain` owns the interfaces
  (`SongRepository`, `AudioPlayer`, `ItunesNetworkDataSource`); implementations live in
  outer modules and are bound with Hilt. The network and audio layers are swappable
  without touching feature code.
- **Room is the single source of truth.** The UI's `PagingSource` reads from Room; a
  `RemoteMediator` fills Room from the network. Going offline just keeps serving the
  cache. Played and (cached) songs are pinned so a new search doesn't evict them.
- **The iTunes Search API has no usable pagination.** So the
  app fetches one large page (`limit=200`, the API max) and paginates **locally** out
  of the Room cache via Paging 3, keeping the lazy‑loading UX while being honest about
  the backend's limits.
- **Stateless, previewable Composables.** Each screen is a stateless `@Composable`
  driven by an immutable UI state from a `@HiltViewModel` exposing `StateFlow` easy
  to preview and to test without Hilt or a device.

## Trade‑offs

- **Multi‑module.** Gains enforced dependency direction,
  parallel/incremental builds, and isolation that makes each layer testable. Costs
  real boilerplate (a build file + Hilt wiring per module) and more indirection to
  navigate. For an app this size a single module would probably enough; the structure is
  chosen to show how the code scales and to keep the network/DB layers swappable.
- **Room as single source of truth (offline‑first).** Gains offline support, a
  consistent reactive cache, and recently‑played for free. Costs entities + mappers +
  schema management and data duplication. I could have saved only the recent play, but I wanted to
  test the remote mediator with the paging3 and reinforce the offline-first requirement.
- **Robolectric for the Compose UI tests.** Gains fast, emulator‑free tests that run in
  the same `./gradlew test` flow (CI‑friendly). Costs fidelity, it simulates the
  framework rather than rendering on a device, so it can't catch device‑specific
  graphics/animation issues. Instrumented tests could be implemented later.
## Tech stack

| Concern     | Choice                                                          |
|-------------|-----------------------------------------------------------------|
| Language    | Kotlin 2.2, coroutines + Flow                                   |
| UI          | Jetpack Compose, Material 3                                     |
| DI          | Hilt                                                            |
| Networking  | Retrofit + OkHttp + kotlinx.serialization                       |
| Persistence | Room                                                            |
| Paging      | Paging 3 (`RemoteMediator` + `PagingSource`)                    |
| Audio       | Media3 / ExoPlayer                                              |
| Images      | Coil                                                            |
| Build       | AGP 9.2, KSP, Gradle version catalog                            |
| Tests       | JUnit4, coroutines‑test, Turbine, Robolectric + Compose UI test |

**SDK:** `minSdk 24`, `compile/target 36`, Java 11.

- **ViewModel / repository / mapper tests**  JUnit4 with coroutines‑test and Turbine,
  using the fakes in `:core:testing`.
- **Compose UI tests**  `SongsScreenTest`, `PlayerScreenTest`, and `AlbumScreenTest`
  run the stateless screens under **Robolectric** with `createComposeRule`, asserting
  rendering and interaction callbacks (search input, row taps, play/pause, retry, etc.).

## Build & run

Requirements: JDK 17+ and the Android SDK.

```bash
# Build the debug APK
./gradlew :app:assembleDebug

# Install on a connected device / emulator
./gradlew :app:installDebug

# Run all unit + UI tests
./gradlew test
```

Or open the project in Android Studio and run the `app` configuration. No API key is
required, the iTunes Search API (`https://itunes.apple.com/`) is public.

## Possible next steps

- **Download for offline playback**  persist a preview to local storage and prefer
  the local file when playing. The `AudioPlayer`/repository boundaries are already
  shaped for this; the download mechanism (a simple streamed fetch, WorkManager, or
  Media3's `DownloadManager`) can be swapped behind a single interface.
