package com.example.greetingcard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

    Surface(
        color = Color.Magenta,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Hi my name is $name and my id is $id!")

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Explicit Intent (Directly targets the class)
            Button(onClick = {
                val explicitIntent = Intent(context, second_activity::class.java)
                context.startActivity(explicitIntent)
            }) {
                Text("Explicit Intent to Second Activity")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Implicit Intent (Uses an abstract action identifier)
            Button(onClick = {
                val implicitIntent = Intent("com.example.greetingcard.ACTION_OPEN_SECOND")
                context.startActivity(implicitIntent)
            }) {
                Text("Implicit Intent to Second Activity")
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
