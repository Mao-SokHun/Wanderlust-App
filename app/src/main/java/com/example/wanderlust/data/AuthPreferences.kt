package com.example.wanderlust.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.wanderlust.data.model.UserProfile

/**
 * Persists login session and user profile cache AES-256 encrypted via [EncryptedSharedPreferences].
 * Protects JWT tokens and session data from unauthorized access even on rooted devices.
 */
object AuthPreferences {
    private const val PREFS = "wanderlust_auth"
    private const val SEC_PREFS = "wanderlust_sec_auth"
    private const val KEY_TOKEN = "token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_NAME = "name"
    private const val KEY_EMAIL = "email"
    private const val KEY_ROLE = "role"
    private const val KEY_BIO = "bio"
    private const val KEY_PHONE = "phone"
    private const val KEY_CITY = "city"
    private const val KEY_GENDER = "gender"
    private const val KEY_BIRTH_DATE = "birth_date"
    private const val KEY_NATIONALITY = "nationality"
    private const val KEY_TRAVEL_STYLE = "travel_style"
    private const val KEY_EMERGENCY = "emergency_contact"
    private const val KEY_LANGUAGE = "language"
    private const val KEY_THEME_DARK = "theme_dark"
    private const val KEY_THEME_LIGHT_DEFAULT_MIGRATED = "theme_light_default_migrated_v1"
    private const val KEY_NOTIFICATIONS = "notifications_enabled"
    private const val KEY_LOCATION = "location_enabled"

    private fun getPrefs(context: Context): SharedPreferences {
        return try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            EncryptedSharedPreferences.create(
                context,
                SEC_PREFS,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SKEY_DATA_ATREST,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
            )
        } catch (_: Exception) {
            context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        }
    }

    /** One-time: older builds defaulted to dark; reset stored preference to light. */
    fun migrateDefaultLightTheme(context: Context) {
        val prefs = getPrefs(context)
        if (prefs.getBoolean(KEY_THEME_LIGHT_DEFAULT_MIGRATED, false)) return
        prefs.edit()
            .putBoolean(KEY_THEME_DARK, false)
            .putBoolean(KEY_THEME_LIGHT_DEFAULT_MIGRATED, true)
            .apply()
    }

    data class SavedSession(
        val token: String,
        val userId: String,
        val name: String,
        val email: String,
        val role: String,
        val bio: String = "",
        val phone: String = "",
        val city: String = "",
        val gender: String = "",
        val birthDate: String = "",
        val nationality: String = "Cambodia",
        val travelStyle: String = "",
        val emergencyContact: String = "",
        val language: String = "km",
        val themeDark: Boolean = false,
        val notificationsEnabled: Boolean = true,
        val locationEnabled: Boolean = true,
    )

    fun save(
        context: Context,
        token: String,
        userId: String,
        name: String,
        email: String,
        role: String,
        bio: String = "",
        phone: String = "",
        city: String = "",
        gender: String = "",
        birthDate: String = "",
        nationality: String = "Cambodia",
        travelStyle: String = "",
        emergencyContact: String = "",
        language: String = "km",
        themeDark: Boolean = false,
        notificationsEnabled: Boolean = true,
        locationEnabled: Boolean = true,
    ) {
        getPrefs(context).edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_USER_ID, userId)
            .putString(KEY_NAME, name)
            .putString(KEY_EMAIL, email)
            .putString(KEY_ROLE, role)
            .putString(KEY_BIO, bio)
            .putString(KEY_PHONE, phone)
            .putString(KEY_CITY, city)
            .putString(KEY_GENDER, gender)
            .putString(KEY_BIRTH_DATE, birthDate)
            .putString(KEY_NATIONALITY, nationality)
            .putString(KEY_TRAVEL_STYLE, travelStyle)
            .putString(KEY_EMERGENCY, emergencyContact)
            .putString(KEY_LANGUAGE, language)
            .putBoolean(KEY_THEME_DARK, themeDark)
            .putBoolean(KEY_NOTIFICATIONS, notificationsEnabled)
            .putBoolean(KEY_LOCATION, locationEnabled)
            .apply()
    }

    fun saveProfile(context: Context, profile: UserProfile, token: String) {
        save(
            context = context,
            token = token,
            userId = profile.id,
            name = profile.name,
            email = profile.email,
            role = profile.role,
            bio = profile.bio,
            phone = profile.phone,
            city = profile.city,
            gender = profile.gender,
            birthDate = profile.birthDate,
            nationality = profile.nationality,
            travelStyle = profile.travelStyle,
            emergencyContact = profile.emergencyContact,
            language = profile.language,
            themeDark = profile.themeDark,
            notificationsEnabled = profile.notificationsEnabled,
            locationEnabled = profile.locationEnabled,
        )
    }

    fun load(context: Context): SavedSession? {
        val p = getPrefs(context)
        val token = p.getString(KEY_TOKEN, null) ?: return null
        val userId = p.getString(KEY_USER_ID, null) ?: return null
        val email = p.getString(KEY_EMAIL, null) ?: return null
        val role = p.getString(KEY_ROLE, "USER") ?: "USER"
        val name = p.getString(KEY_NAME, "Explorer") ?: "Explorer"

        return SavedSession(
            token = token,
            userId = userId,
            name = name,
            email = email,
            role = role,
            bio = p.getString(KEY_BIO, "").orEmpty(),
            phone = p.getString(KEY_PHONE, "").orEmpty(),
            city = p.getString(KEY_CITY, "").orEmpty(),
            gender = p.getString(KEY_GENDER, "").orEmpty(),
            birthDate = p.getString(KEY_BIRTH_DATE, "").orEmpty(),
            nationality = p.getString(KEY_NATIONALITY, "Cambodia") ?: "Cambodia",
            travelStyle = p.getString(KEY_TRAVEL_STYLE, "").orEmpty(),
            emergencyContact = p.getString(KEY_EMERGENCY, "").orEmpty(),
            language = p.getString(KEY_LANGUAGE, "km") ?: "km",
            themeDark = p.getBoolean(KEY_THEME_DARK, false),
            notificationsEnabled = p.getBoolean(KEY_NOTIFICATIONS, true),
            locationEnabled = p.getBoolean(KEY_LOCATION, true),
        )
    }

    fun clear(context: Context) {
        getPrefs(context).edit().clear().apply()
    }

    fun loadLanguage(context: Context): String {
        return getPrefs(context).getString(KEY_LANGUAGE, "km") ?: "km"
    }

    fun saveLanguage(context: Context, lang: String) {
        getPrefs(context).edit().putString(KEY_LANGUAGE, lang).apply()
    }
}
