package com.example.windy.alerts.view

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.windy.database.Alerts

class AlertScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun schedule(alerts: Alerts){
        val intent = Intent(context, Notifications::class.java)
        var title = alerts.title
        var message = alerts.message
        var id = alerts.id
        intent.putExtra("Alert Title",title)
        intent.putExtra("Alert Message",message)
        intent.putExtra("Alert Id",id)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alerts.time,
            PendingIntent.getBroadcast(
                context,
                alerts.time.toInt(),
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
    }

    fun cancel(alerts: Alerts){
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alerts.time.toInt(),
                Intent(context,Notifications::class.java),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
    }
}