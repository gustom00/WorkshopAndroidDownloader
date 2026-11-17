# WorkshopOnAndroid

WorkshopOnAndroid is an Android app for browsing and downloading Steam Workshop mods. Browse supported games, search Workshop items, inspect details, and manage downloads. Public items can be downloaded anonymously; Steam sign-in is available for content that requires an authenticated account.

[English](README.md) | [Bahasa Indonesia](README.id.md)

## Features

| Area | Details |
| --- | --- |
| Game library | Browse featured Workshop games, search Steam games, or add a game by `AppID`. |
| Workshop browser | Search and page through items, then view item details. |
| Downloads | Queue downloads, track progress and logs, pause or resume tasks, and manage exported files in the mod library. |
| Steam sign-in | Save multiple Steam accounts and bind authenticated downloads to the account active when a task is queued. |
| ADB automation | Gradle tasks can start a download directly on a connected device and pull its logs. |

The `:steam-protocol` module handles Steam CM connections, authentication, content-server discovery, and CDN access. The `:workshop-core` module parses manifests, downloads and verifies chunks, and assembles files.

## Quick start

### Requirements

- JDK 17 or newer (the project currently builds with JDK 21)
- Android Studio and Android SDK
- Android 8.0 (API 26) or newer
- Network access to Steam Store, Steam Community, and Steam Web API
- `adb` and a connected device for device automation

### Build and install a debug APK

```powershell
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:installDebug
```

### Use the app

1. Open the game library and tap `+`.
2. Add a supported game using featured games, search, or its `AppID`.
3. Open the game's Workshop page and browse or search for an item.
4. Open an item and tap **Download** to add it to the download center.
5. Follow progress in the download center. Exported files are managed in the mod library.

The app's language can be changed in **Settings**. English and Bahasa Indonesia are available. This setting changes the app interface, not the language of Steam content.

### Download locations

- Private cache: `/data/user/0/top.apricityx.workshop/files/workshop/<AppID>/<PublishedFileId>/`
- Exported files: `/sdcard/Download/workshop/<Game>/<Mod>/<Version>/`

For a single-file mod, the app prefers a readable title or filename from Steam metadata. Different versions are kept in separate directories.

## Build configuration

| Module | Responsibility |
| --- | --- |
| `:app` | Compose UI, navigation, game library, settings, download center, and file export |
| `:workshop-core` | Workshop metadata, direct and UGC downloads, chunk processing, verification, and assembly |
| `:steam-protocol` | Steam CM WebSocket sessions, authentication, content-server requests, and CDN credentials |

The app uses `minSdk 26`, `targetSdk 36`, and `compileSdk 36`. Java and Kotlin bytecode target version 17. Settings let you adjust download threads (1–8), concurrent tasks (1–3), theme, and Steam accounts.

## Release builds

Pushing a `v*` tag runs the release workflow in `.github/workflows/release.yml`. It builds a signed release APK, uploads the artifact, and creates or updates a GitHub Release. Signing credentials are supplied through the `release-signing` GitHub Environment. See [release automation setup](docs/release-automation/README.md).

Prepare release notes and a tag with:

```powershell
scripts\prepare-release.bat
```

## ADB download automation

The `workshopDownloadAndPullLogs` task installs the latest debug APK, clears cached data for the selected item, starts a download over ADB, waits for completion or timeout, and pulls the download log, Logcat, metadata, and result files.

```powershell
.\gradlew.bat :app:workshopStartDownload -PworkshopAppId=646570 -PpublishedFileId=3677098410 -PdeviceSerial=<adb-serial>
.\gradlew.bat :app:workshopPullLogs -PworkshopAppId=646570 -PpublishedFileId=3677098410 -PdeviceSerial=<adb-serial>
.\gradlew.bat :app:workshopDownloadAndPullLogs -PworkshopAppId=646570 -PpublishedFileId=3677098410 -PdeviceSerial=<adb-serial> -PdownloadTimeoutSeconds=180 --console=plain
```

Optional parameters include `-PdeviceSerial`, `-PdownloadTimeoutSeconds`, `-PpollIntervalMillis`, and `-PlogsDir`. Logs default to `build/workshop-adb/app-<AppID>-file-<PublishedFileId>-<timestamp>/`.

## Limitations

- `Collection` downloads and QR-code sign-in are not supported.
- Authenticated downloads depend on the Steam account's access to the item and Steam's authentication responses.
- Workshop browsing and item details depend on Steam pages and APIs, which may change.
- Unstable access to Steam can cause game search, Workshop browsing, or downloads to fail or time out.
- Built-in acceleration covers Steam Community, Store/API, and image routes. Steam CM connections still require the current network to reach at least one available Steam node.

## License

This project is licensed under the [Apache License 2.0](LICENSE).
