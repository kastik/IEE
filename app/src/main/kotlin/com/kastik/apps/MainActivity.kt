package com.kastik.apps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.kastik.apps.navigation.NavHost
import com.kastik.apps.ui.theme.AppsAboardTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppsAboardTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    NavHost()
                }

            }
        }
    }
}