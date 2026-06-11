package com.ucsm.budgetme

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotification : FirebaseMessagingService() {

    companion object {
        private const val TAG = "PushNotification"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "From: " + remoteMessage.from)

        val titulo = remoteMessage.notification?.title ?: remoteMessage.data["title"] ?: "Notificación"
        val mensaje = remoteMessage.notification?.body ?: remoteMessage.data["body"] ?: "Nuevo mensaje"

        Log.d(TAG, "Message: $mensaje")
        mostrarNotificacion(titulo, mensaje)
    }

    private fun mostrarNotificacion(titulo: String, mensaje: String) {
        val channelId = "canal_notificaciones"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as android.app.NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val canal = android.app.NotificationChannel(
                channelId,
                "Notificaciones",
                android.app.NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(canal)
        }

        val notificacion = androidx.core.app.NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1, notificacion)
    }
}