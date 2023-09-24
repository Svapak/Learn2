package com.example.learn2.notification

data class PushNotification(

    val data: NotificationData,
    val to :String?="",
)
