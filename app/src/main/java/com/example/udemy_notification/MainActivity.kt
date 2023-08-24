package com.example.udemy_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import androidx.databinding.DataBindingUtil
import com.example.udemy_notification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val channelId = "com.example.udemy_notification.channel1"
    private var notificationManager: NotificationManager? = null
    private val KEY_REPLY = "key_reply"

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(channelId, "DemoChannel", "this is a demo")

        binding.btnBtn.setOnClickListener {
            displayNotification()
        }
    }

    private fun displayNotification() {

        val notificationId = 45
        val tapResultIntent = Intent(this, SecondActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, tapResultIntent, PendingIntent.FLAG_MUTABLE)

        //reply
        val remoteInput: RemoteInput = RemoteInput.Builder(KEY_REPLY).run {
            setLabel("Insert your name here")
            build()
        }
        val replyAction =
            NotificationCompat.Action.Builder(0, "REPLY", pendingIntent)
                .addRemoteInput(remoteInput)
                .build()


        val intent2 = Intent(this, DetailsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
        }
        val pendingIntent2 = PendingIntent.getActivity(this, 0, intent2, PendingIntent.FLAG_MUTABLE)
        val action2 = NotificationCompat.Action.Builder(0, "Details", pendingIntent2).build()

        val intent3 = Intent(this, SettingsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
        }
        val pendingIntent3 = PendingIntent.getActivity(this, 0, intent3, PendingIntent.FLAG_MUTABLE)
        val action3 = NotificationCompat.Action.Builder(0, "Settings", pendingIntent3).build()

        val notification = NotificationCompat.Builder(this@MainActivity, channelId)
            .setContentTitle("Demo Title")
            .setContentText("This is a demo notification")
            .setSmallIcon(androidx.core.R.drawable.notification_icon_background)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(action2)
            .addAction(action3)
            .addAction(replyAction)
            .build()

        notificationManager?.notify(notificationId, notification)

    }

    private fun createNotificationChannel(id: String, name: String, channelDescription: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importanceLevel = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importanceLevel).apply {
                this.description = channelDescription
            }
            notificationManager?.createNotificationChannel(channel)

        }
    }

}