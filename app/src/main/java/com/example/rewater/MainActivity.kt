package com.example.rewater

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var counter by mutableIntStateOf(0)
    private var isAwake by mutableStateOf(true)
    private var lastNotificationTime by mutableLongStateOf(0L)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReWaterApp(counter = counter,
                isAwake = isAwake,
                onCounterChange = { newCounter ->
                    counter = newCounter
                    savePreferences(counter, isAwake, lastNotificationTime)
                },
                onAwakeStateChange = { newAwakeState ->
                    isAwake = newAwakeState
                    savePreferences(counter, isAwake, lastNotificationTime)
                }
            )
        }

        sharedPreferences = getSharedPreferences("ReWaterPrefs", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        loadPreferences()

        updateUI()
    }

    private fun savePreferences(counter: Int, isAwake: Boolean, lastNotificationTime: Long) {
        editor.putInt("counter", counter)
        editor.putBoolean("isAwake", isAwake)
        editor.putLong("lastNotificationTime", lastNotificationTime)
        editor.apply()
    }

    private fun loadPreferences() {
        counter = sharedPreferences.getInt("counter", 0)
        isAwake = sharedPreferences.getBoolean("isAwake", true)
        lastNotificationTime = sharedPreferences.getLong("lastNotificationTime", 0L)
    }

    private fun updateUI() {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReWaterApp(counter: Int, isAwake: Boolean, onCounterChange: (Int) -> Unit, onAwakeStateChange: (Boolean) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("ReWater") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Water Counter = $counter",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { if (counter > 0) onCounterChange(counter - 1) }) {
                    Text("Drank Water (-)")
                }
                Button(onClick = { onCounterChange(counter + 1) }) {
                    Text("Missed Reminder (+)")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Awake State: ")
                Switch(
                    checked = isAwake,
                    onCheckedChange = { onAwakeStateChange(it) }
                )
                Text(if (isAwake) "Awake" else "Asleep")
            }
        }
    }
}

