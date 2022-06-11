package com.chocomiruku.myhome.data.firebase

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.chocomiruku.myhome.MainActivity
import com.chocomiruku.myhome.R
import com.chocomiruku.myhome.data.Resource
import com.chocomiruku.myhome.domain.usecase.user.GetCurrentUserUseCase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var getCurrentUserUseCase: GetCurrentUserUseCase

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i(TAG, "New token was generated: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.notification?.let { notification ->
            CoroutineScope(Dispatchers.IO).launch {
                getCurrentUserUseCase.invoke().collect { user ->
                    if (user is Resource.Success && user.data?.notifications == true) {
                        sendNotification(notification)
                    }

                }
            }
        }
    }

    private fun sendNotification(notification: RemoteMessage.Notification) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setChannelId(CHANNEL_ID)
            .setContentTitle(notification.title)
            .setStyle(NotificationCompat.BigTextStyle())
            .setContentText(notification.body)
            .setSmallIcon(R.drawable.ic_home)
            .setColor(getColor(R.color.green_500))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {
        private const val CHANNEL_ID = "default_channel_id"
        private const val TAG = "MessagingService"
    }
}