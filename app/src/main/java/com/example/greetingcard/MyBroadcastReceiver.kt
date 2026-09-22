package com.example.greetingcard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Check if the received action matches our custom action string
        if (intent.action == "com.example.MY_ACTION") {
            Toast.makeText(context, "Broadcast received!", Toast.LENGTH_SHORT).show()
        }
    }
}
