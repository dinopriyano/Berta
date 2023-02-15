package com.dino.berta.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.dino.berta.ui.screen.BertaApp
import com.dino.berta.ui.theme.BertaTheme
import com.google.accompanist.adaptive.calculateDisplayFeatures
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterial3Api @ExperimentalMaterial3WindowSizeClassApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      BertaTheme { // A surface container using the 'background' color from the theme
        val windowSize = calculateWindowSizeClass(this)
        val displayFeatures = calculateDisplayFeatures(this)

        BertaApp(windowSize = windowSize, displayFeatures = displayFeatures)
      }
    }
  }
}