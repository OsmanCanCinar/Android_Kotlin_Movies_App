package com.osmancancinar.moviesapp.viewModels

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.osmancancinar.moviesapp.R
import com.osmancancinar.moviesapp.ui.MainActivity

class MainActivityViewModel(private val app: Application) : BaseViewModel(app) {

    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName"
    val NOTIFITACTION_ID = 0

    fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT).apply {
                lightColor = Color.GREEN
                enableLights(true)
            }
            val manager = app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun sendNotification(activity: Activity) {
        val  intent = Intent(activity,MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(activity).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification = NotificationCompat.Builder(app.applicationContext,CHANNEL_ID)
            .setContentTitle("Movie Tip")
            .setContentText("You can check lots of movies before going to theatre!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = NotificationManagerCompat.from(app)
        notificationManager.notify(NOTIFITACTION_ID,notification)
    }
}