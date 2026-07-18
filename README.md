# Wanderlust (Android)

Cambodia travel marketplace app.

- **Versioning:** see [VERSIONING.md](VERSIONING.md) (Semantic Versioning)
- **Downloads:** [Releases](https://github.com/Mao-SokHun/Wanderlust-App/releases) · [Install page](https://wanderlust-api-dm3y.onrender.com/download/)
- **API:** [wanderlust-api](https://github.com/Mao-SokHun/wanderlust-api) (private)

## Build release APK

1. Copy `local.properties` (Maps / Google / Facebook keys) and `keystore.properties` + `keystore/`.
2. `./gradlew :app:assembleRelease`
3. Publish APK only when SemVer says you should (feature / fix / breaking).

## Note

Older experimental releases lived under `Mao-SokHun/Wanderlust` (1.2.x–1.3.x).  
This repo restarts the public line at **1.0.0**.
