package com.example.learn2

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.learn2.bottomnavigationfragments.chats
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class MYFirebaseMessagingService: FirebaseMessagingService() {

    private val channelId = "learn2"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val intent=Intent(this, chats::class.java)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        createNotificationChannel(manager as NotificationManager)

        val intent1 = PendingIntent.getActivities(this,0,
        arrayOf(intent),PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this,channelId)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.notificationicon)
            .setAutoCancel(true)
            .setContentIntent(intent1)
            .build()


        manager.notify(Random().nextInt(),notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(manager: NotificationManager){

        val channel =NotificationChannel(channelId, "learn2chats",
        NotificationManager.IMPORTANCE_HIGH)

        channel.description= "new chat"
        channel.enableLights(true)

        manager.createNotificationChannel(channel)
    }
}