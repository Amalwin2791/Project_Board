package com.example.boardsdraft.view

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.contentValuesOf
import com.example.boardsDraft.R
import com.example.boardsdraft.view.activities.SplashScreen

const val notificationID = 1
const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class Notification: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

        val startIntent = Intent(context,SplashScreen::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            startIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification  = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle(intent?.getStringExtra(titleExtra))
            .setContentText(intent?.getStringExtra(messageExtra))
            .setContentIntent(pendingIntent)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID,notification)
    }


}