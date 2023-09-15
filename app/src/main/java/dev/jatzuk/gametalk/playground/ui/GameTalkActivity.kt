package dev.jatzuk.gametalk.playground.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.jatzuk.gametalk.playground.ui.theme.GameTalkTheme

class GameTalkActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      GameTalkTheme {
        GameTalkAppEntryPoint(viewModel())
      }
    }
  }
}
