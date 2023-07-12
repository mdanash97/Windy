package com.example.windy.alerts.view

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.windy.MainActivity
import com.example.windy.alerts.viewmodel.AlertViewModel
import com.example.windy.alerts.viewmodel.AlertsViewModelFactory
import com.example.windy.database.Alerts
import com.example.windy.database.AppDatabase
import com.example.windy.database.ConcreteLocalSource
import com.example.windy.databinding.ActivityAlertBinding
import com.example.windy.model.Repository
import com.example.windy.network.WeatherClient
import java.util.Calendar
import java.util.Date

class AlertActivity : AppCompatActivity() {

    private lateinit var alertActivityBinding: ActivityAlertBinding
    lateinit var alertScheduler: AlertScheduler
    lateinit var alerts: Alerts
    lateinit var alertViewModelFactory: AlertsViewModelFactory
    lateinit var alertViewModel: AlertViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertActivityBinding = ActivityAlertBinding.inflate(layoutInflater)
        setContentView(alertActivityBinding.root)
        alertScheduler = AlertScheduler(this)

        alertViewModelFactory = AlertsViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(),
                ConcreteLocalSource(
                    AppDatabase.getInstance(this).getLocationDAo(),
                    AppDatabase.getInstance(this).getAlertsDAo())
            ))
        alertViewModel = ViewModelProvider(this,alertViewModelFactory)[AlertViewModel::class.java]

        val intentToHome = Intent(this@AlertActivity,MainActivity::class.java)
        intentToHome.putExtra("Saved","Alert")
        createNotificationChannel()

        alertActivityBinding.setAlert.setOnClickListener {
            var title = alertActivityBinding.titleText.text.toString()
            var message = alertActivityBinding.messageText.text.toString()
            var time = getTime()
            alerts = Alerts(title = title, message = message, time =time )
            alertScheduler.schedule(alerts)
            alertViewModel.insertAlert(alerts)
            startActivity(intentToHome)
        }

        alertActivityBinding.backButton.setOnClickListener {
            startActivity(intentToHome)
        }
    }

    private fun createNotificationChannel() {
        val name = "Notif Channel"
        val desc = "A description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID,name,importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
    private fun getTime(): Long {
        val minute = alertActivityBinding.timePicker.minute
        val hour = alertActivityBinding.timePicker.hour
        val day = alertActivityBinding.datePicker.dayOfMonth
        val month = alertActivityBinding.datePicker.month
        val year = alertActivityBinding.datePicker.year
        val calender = Calendar.getInstance()
        calender.set(year,month,day,hour,minute)
        return calender.timeInMillis
    }

}


//    private fun scheduleNotification() {
//        val intent = Intent(applicationContext, Notifications::class.java)
//        val title = alertActivityBinding.titleText.text.toString()
//        val message = alertActivityBinding.messageText.text.toString()
//        intent.putExtra(titleExtra,title)
//        intent.putExtra(messageExtra,message)
//
//        val pendingIntent = PendingIntent.getBroadcast(
//            applicationContext,
//            notificationsID,
//            intent,
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )
//
//        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val time = getTime()
//        alarmManager.setExactAndAllowWhileIdle(
//            AlarmManager.RTC_WAKEUP,
//            time,
//            pendingIntent
//        )
//        showAlert(time,title,message)
//    }
//
//    private fun showAlert(time: Long, title: String, message: String) {
//        val date = Date(time)
//        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
//        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)
//
//        val dialog = AlertDialog.Builder(this).setTitle("Scheduled ALer").setMessage(
//            "Title: "+title+"\nMessage: "+message+"\nAt: "+dateFormat.format(date)+" "+timeFormat.format(date)
//        ).setPositiveButton("Okay"){_,_->}
//        dialog.show()
//
//    }
//
//    private fun getTime(): Long {
//        val minute = alertActivityBinding.timePicker.minute
//        val hour = alertActivityBinding.timePicker.hour
//        val day = alertActivityBinding.datePicker.dayOfMonth
//        val month = alertActivityBinding.datePicker.month
//        val year = alertActivityBinding.datePicker.year
//
//        val calender = Calendar.getInstance()
//        calender.set(year,month,day,hour,minute)
//        return calender.timeInMillis
//    }
//
//    private fun createNotificationChannel() {
//        val name = "Notif Channel"
//        val desc = "A description of the Channel"
//        val importance = NotificationManager.IMPORTANCE_DEFAULT
//        val channel = NotificationChannel(channelID,name,importance)
//        channel.description = desc
//        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(channel)
//    }