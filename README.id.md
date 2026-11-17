# WorkshopOnAndroid

WorkshopOnAndroid adalah aplikasi Android untuk menjelajahi dan mengunduh mod Steam Workshop. Jelajahi game yang didukung, cari item Workshop, lihat detail, dan kelola unduhan. Item publik dapat diunduh tanpa login; login Steam tersedia untuk konten yang memerlukan akun terautentikasi.

[English](README.md) | [Bahasa Indonesia](README.id.md)

## Fitur

| Area | Keterangan |
| --- | --- |
| Pustaka game | Jelajahi game Workshop pilihan, cari game Steam, atau tambahkan game dengan `AppID`. |
| Penjelajah Workshop | Cari dan jelajahi daftar item, lalu lihat detailnya. |
| Unduhan | Antrekan unduhan, pantau progres dan log, jeda atau lanjutkan tugas, dan kelola file hasil ekspor di pustaka mod. |
| Login Steam | Simpan beberapa akun Steam dan ikat unduhan terautentikasi ke akun yang aktif saat tugas dimasukkan ke antrean. |
| Otomatisasi ADB | Tugas Gradle dapat memulai unduhan langsung di perangkat yang tersambung dan mengambil lognya. |

Modul `:steam-protocol` menangani koneksi Steam CM, autentikasi, pencarian server konten, dan akses CDN. Modul `:workshop-core` membaca manifest, mengunduh dan memverifikasi chunk, lalu menyusun file.

## Mulai cepat

### Persyaratan

- JDK 17 atau lebih baru (proyek saat ini dibangun dengan JDK 21)
- Android Studio dan Android SDK
- Android 8.0 (API 26) atau lebih baru
- Akses jaringan ke Steam Store, Steam Community, dan Steam Web API
- `adb` dan perangkat tersambung untuk otomatisasi perangkat

### Build dan pasang APK debug

```powershell
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:installDebug
```

### Menggunakan aplikasi

1. Buka pustaka game lalu ketuk `+`.
2. Tambahkan game yang didukung melalui daftar pilihan, pencarian, atau `AppID`-nya.
3. Buka halaman Workshop game tersebut, lalu jelajahi atau cari item.
4. Buka item dan ketuk **Unduh** untuk menambahkannya ke pusat unduhan.
5. Pantau progres di pusat unduhan. File hasil ekspor dikelola di pustaka mod.

Bahasa aplikasi dapat diubah di **Pengaturan**. Bahasa Inggris dan Bahasa Indonesia tersedia. Pilihan ini mengubah antarmuka aplikasi, bukan bahasa konten Steam.

### Lokasi unduhan

- Cache privat: `/data/user/0/top.apricityx.workshop/files/workshop/<AppID>/<PublishedFileId>/`
- File hasil ekspor: `/sdcard/Download/workshop/<Game>/<Mod>/<Version>/`

Untuk mod satu file, aplikasi mengutamakan judul atau nama file yang lebih mudah dibaca dari metadata Steam. Versi yang berbeda disimpan di direktori terpisah.

## Konfigurasi build

| Modul | Tanggung jawab |
| --- | --- |
| `:app` | UI Compose, navigasi, pustaka game, pengaturan, pusat unduhan, dan ekspor file |
| `:workshop-core` | Metadata Workshop, unduhan langsung dan UGC, pemrosesan chunk, verifikasi, dan penyusunan file |
| `:steam-protocol` | Sesi Steam CM WebSocket, autentikasi, permintaan server konten, dan kredensial CDN |

Aplikasi menggunakan `minSdk 26`, `targetSdk 36`, dan `compileSdk 36`. Target bytecode Java dan Kotlin adalah versi 17. Pengaturan aplikasi menyediakan pilihan jumlah thread unduhan (1–8), tugas bersamaan (1–3), tema, dan akun Steam.

## Build rilis

Push tag `v*` akan menjalankan workflow rilis di `.github/workflows/release.yml`. Workflow membuat APK release bertanda tangan, mengunggah artefak, lalu membuat atau memperbarui GitHub Release. Kredensial signing diberikan melalui GitHub Environment `release-signing`. Lihat [panduan otomatisasi rilis](docs/release-automation/README.md).

Siapkan catatan rilis dan tag dengan:

```powershell
scripts\prepare-release.bat
```

## Otomatisasi unduhan ADB

Tugas `workshopDownloadAndPullLogs` memasang APK debug terbaru, membersihkan cache untuk item yang dipilih, memulai unduhan melalui ADB, menunggu sampai selesai atau timeout, lalu mengambil log unduhan, Logcat, metadata, dan file hasil.

```powershell
.\gradlew.bat :app:workshopStartDownload -PworkshopAppId=646570 -PpublishedFileId=3677098410 -PdeviceSerial=<adb-serial>
.\gradlew.bat :app:workshopPullLogs -PworkshopAppId=646570 -PpublishedFileId=3677098410 -PdeviceSerial=<adb-serial>
.\gradlew.bat :app:workshopDownloadAndPullLogs -PworkshopAppId=646570 -PpublishedFileId=3677098410 -PdeviceSerial=<adb-serial> -PdownloadTimeoutSeconds=180 --console=plain
```

Parameter opsional mencakup `-PdeviceSerial`, `-PdownloadTimeoutSeconds`, `-PpollIntervalMillis`, dan `-PlogsDir`. Log disimpan secara default di `build/workshop-adb/app-<AppID>-file-<PublishedFileId>-<timestamp>/`.

## Batasan

- Unduhan `Collection` dan login menggunakan kode QR belum didukung.
- Unduhan terautentikasi bergantung pada akses akun Steam ke item dan hasil autentikasi dari Steam.
- Penjelajahan Workshop dan detail item bergantung pada halaman serta API Steam yang dapat berubah.
- Koneksi yang tidak stabil ke Steam dapat menyebabkan pencarian game, penjelajahan Workshop, atau unduhan gagal maupun timeout.
- Akselerasi bawaan mencakup Steam Community, Store/API, dan jalur gambar. Koneksi Steam CM tetap memerlukan jaringan yang dapat menjangkau setidaknya satu node Steam yang tersedia.

## Lisensi

Proyek ini menggunakan [Apache License 2.0](LICENSE).
