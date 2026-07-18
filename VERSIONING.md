# Versioning (Semantic Versioning)

Format: **X.Y.Z** (example: `1.4.2`)

| Part | Name | When to bump | Example |
|------|------|--------------|---------|
| **X** | Major | Breaking changes / full redesign / API incompatible with old apps | `1.9.0` → `2.0.0` |
| **Y** | Minor | **New feature** (backward-compatible) | `1.0.5` → `1.1.0` |
| **Z** | Patch | **Bug fix** / small polish / security patch (no new feature) | `1.0.0` → `1.0.1` |

## Rules for Wanderlust releases

1. **Do not** cut a GitHub Release for every tiny WIP change.
2. Bump **only when you ship** a Release APK users should install.
3. New feature → bump **Minor** (`1.0.0` → `1.1.0`), reset patch to `0`.
4. Bug fix only → bump **Patch** (`1.0.0` → `1.0.1`).
5. Breaking change → bump **Major** (`1.3.2` → `2.0.0`), reset Y and Z to `0`.
6. Always increase Android `versionCode` by at least `1` on every published APK.
7. After publishing, set Render:
   - `APP_VERSION_NAME` = same as `versionName`
   - `APP_VERSION_CODE` = same as `versionCode`
   - Upload `wanderlust-latest.apk` on the GitHub Release

## Current line

- Repo: `Mao-SokHun/Wanderlust-App`
- First public line: **1.0.0** (`versionCode` 100)
- **1.1.0** (`versionCode` 101): production billing — sandbox gated off in prod; Bakong KHQR-only for businesses
- **1.2.0** (`versionCode` 102): request-to-book, sticky Book CTA, guest My Requests, business inbox, operator photos on detail
