package com.example.learn2.notification

import android.telecom.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {


    @Headers("Content-Type:application/json",
        "Authorization:key=AAAASSY_EbE:APA91bGwZ93I-ddB-p9cfIYgn2yuXd-3ea3o013iR641KKMklIeK11PEwl3wNSwaBYvZSFDXNzSfJzWvOYnPTuffXS1BxNBM23o1bWiS9vLzfKAgSy4Tc1U7Zm_IsoXIypxJXoCmSaN1")
    @POST("fcm/send")
    fun sendNotification(@Body notification: PushNotification)
            : retrofit2.Call<PushNotification>
}