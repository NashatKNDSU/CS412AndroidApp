package com.example.greetingcard

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.greetingcard.ui.theme.GreetingCardTheme
import com.example.greetingcard.second_activity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GreetingCardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        id = 1180902,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, id: Int, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val customAction = "com.example.MY_ACTION"

    // Compose states to track service connection updates dynamically
    var gradeText by remember { mutableStateOf("Grade: --") }
    var isBound by remember { mutableStateOf(false) }

    // 1. Setup the ServiceConnection Interface
    val connection = remember {
        object : ServiceConnection {
            override fun onServiceConnected(className: ComponentName, service: IBinder) {
                val binder = service as MyForegroundService.LocalBinder
                val boundService = binder.getService()
                isBound = true

                // Fetch the grade string and update our Compose state variable
                gradeText = boundService.getMyGrade()
            }

            override fun onServiceDisconnected(arg0: ComponentName) {
                isBound = false
            }
        }
    }

    // 2. Lifecycle management for the Broadcast Receiver and Unbinding
    DisposableEffect(Unit) {
        val receiver = MyBroadcastReceiver()
        val filter = IntentFilter(customAction)

        // Register the dynamic broadcast receiver safely based on Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            context.registerReceiver(receiver, filter)
        }

        // Acts like onDestroy/onStop to clean up resources and prevent memory leaks
        onDispose {
            context.unregisterReceiver(receiver)
            if (isBound) {
                context.unbindService(connection)
            }
        }
    }

    // 3. Runtime Permission Prompt launcher for Android 13+ Notification Requirements
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Notification permission is required for the Foreground Service!", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // 4. UI Layout Composition Structure
    Surface(
        color = Color.Magenta,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Hi my name is $name and my id is $id!")

            Spacer(modifier = Modifier.height(16.dp))

            // Original Explicit Intent Button
            Button(
                onClick = {
                    val explicitIntent = Intent(context, second_activity::class.java)
                    context.startActivity(explicitIntent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Explicit Intent to Second Activity")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Original Implicit Intent Button
            Button(
                onClick = {
                    val implicitIntent = Intent("com.example.greetingcard.ACTION_OPEN_SECOND")
                    context.startActivity(implicitIntent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Implicit Intent to Second Activity")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- NEW CODE: Service & Broadcast controls ---

            // Start Service Button
            Button(
                onClick = {
                    val intent = Intent(context, MyForegroundService::class.java)
                    ContextCompat.startForegroundService(context, intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Service")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bind Service Button layout with the text aligned directly next to it horizontally
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val intent = Intent(context, MyForegroundService::class.java)
                        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Bind Service")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = gradeText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Send Custom Broadcast Button
            Button(
                onClick = {
                    val intent = Intent(customAction).apply {
                        setPackage(context.packageName) // Matches explicit intent security requirements
                    }
                    context.sendBroadcast(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send Broadcast")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GreetingCardTheme {
        Greeting("Nashat", id = 1180902)
    }
}
