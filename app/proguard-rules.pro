# ─────────────────────────────────────────────────────────────────────────────
# Wanderlust — Production R8 / ProGuard Obfuscation & Security Rules
# Prevents reverse-engineering and APK decompilation by hackers.
# ─────────────────────────────────────────────────────────────────────────────

# Keep data models so Gson serialization doesn't break
-keep class com.example.wanderlust.data.model.** { *; }

# Keep Retrofit API interfaces & response classes
-keep interface com.example.wanderlust.data.remote.** { *; }
-keepclassmembers class * {
    @retrofit2.http.* <methods>;
}

# Retrofit & Gson rules
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes *Annotation*
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**

# Gson specific rules
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Google Maps & Play Services
-keep class com.google.android.gms.maps.** { *; }
-keep class com.google.android.gms.location.** { *; }
-dontwarn com.google.android.gms.**

# AndroidX Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.**

# Jetpack Compose
-keep class androidx.compose.** { *; }

# Keep SessionManager & MaskUtils
-keep class com.example.wanderlust.data.SessionManager { *; }
-keep class com.example.wanderlust.util.MaskUtils { *; }
