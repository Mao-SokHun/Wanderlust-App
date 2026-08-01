package com.example.wanderlust.messaging

/**
 * WanderlustFirebaseMessagingService
 *
 * This class is the FULL Firebase Cloud Messaging integration.
 * To activate it:
 *
 * STEP 1: Go to https://console.firebase.google.com/
 *         - Create a new project called "Wanderlust"
 *         - Add an Android app with package name: com.example.wanderlust
 *         - Download google-services.json and place it at:
 *           /Wanderlust-App/app/google-services.json
 *
 * STEP 2: In app/build.gradle.kts:
 *         - Change: id("com.google.gms.google-services") apply false
 *         - To:     id("com.google.gms.google-services")
 *         - Uncomment the firebase-bom and firebase-messaging-ktx lines
 *
 * STEP 3: In the root build.gradle.kts (or settings.gradle.kts), add:
 *         id("com.google.gms.google-services") version "4.4.2" apply false
 *
 * STEP 4: Uncomment the class below and delete these comments.
 *
 * STEP 5: In server.js, replace the FCM-MOCK console.log with real FCM API calls:
 *         POST to https://fcm.googleapis.com/v1/projects/{PROJECT_ID}/messages:send
 *         using the Firebase Admin SDK or raw HTTP with a service account token.
 */

// import com.google.firebase.messaging.FirebaseMessagingService
// import com.google.firebase.messaging.RemoteMessage
//
// class WanderlustFirebaseMessagingService : FirebaseMessagingService() {
//
//     override fun onMessageReceived(message: RemoteMessage) {
//         super.onMessageReceived(message)
//         val title = message.notification?.title ?: message.data["title"] ?: "Wanderlust"
//         val body = message.notification?.body ?: message.data["body"] ?: ""
//         val channel = when (message.data["type"]) {
//             "chat"  -> NotificationHelper.CHANNEL_CHAT_ID
//             "promo" -> NotificationHelper.CHANNEL_PROMO_ID
//             else    -> NotificationHelper.CHANNEL_ALERT_ID
//         }
//         NotificationHelper.showNotification(this, title, body, channel)
//     }
//
//     override fun onNewToken(token: String) {
//         super.onNewToken(token)
//         // TODO: Send this token to your Node.js server so the backend can target this device:
//         // POST /api/auth/fcm-token  { token: token }
//         android.util.Log.d("FCM", "New token: $token")
//     }
// }
