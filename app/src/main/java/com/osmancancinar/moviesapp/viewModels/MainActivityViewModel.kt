package com.osmancancinar.moviesapp.viewModels

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.osmancancinar.moviesapp.R
import com.osmancancinar.moviesapp.ui.MainActivity

class MainActivityViewModel(private val app: Application) : BaseViewModel(app) {

    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "MoviesApp"
    val NOTIFITACTION_ID = 0

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.GREEN
                enableLights(true)
            }
            val manager = app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun sendNotification(activity: Activity) {
        val msg = app.getString(R.string.notification_msg)

        val intent = Intent(activity, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(activity).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification = NotificationCompat.Builder(app.applicationContext, CHANNEL_ID)
            .setContentTitle(app.getString(R.string.notification_tip))
            .setContentText(msg)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(msg))
            .build()

        val notificationManager = NotificationManagerCompat.from(activity)
        notificationManager.notify(NOTIFITACTION_ID, notification)
    }
}