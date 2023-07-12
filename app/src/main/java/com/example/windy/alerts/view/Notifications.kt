package com.example.windy.alerts.view

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.windy.R


const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class Notifications : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notification  = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_baseline_circle_notifications_24)
            .setContentTitle(intent.getStringExtra("Alert Title"))
            .setContentText(intent.getStringExtra("Alert Message"))
            .build()
        val manger = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manger.notify(intent.getIntExtra("Alert Id",0),notification)

    }
}