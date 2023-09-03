package dev.jatzuk.gametalk.playground.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import dev.jatzuk.gametalk.playground.entrypoint.surface.GameSurfaceView
import dev.jatzuk.gametalk.playground.ui.RenderMode
import dev.jatzuk.gametalk.playground.entrypoint.view.GameView

@Composable
fun ViewContainer(
  viewType: String,
  modifier: Modifier = Modifier
) {
  AndroidView(
    modifier = modifier
      .fillMaxSize(),
    factory = { context ->
      if (viewType.equals(RenderMode.VIEW.name, true)) {
        GameView(context)
      } else {
        GameSurfaceView(context)
      }
    }
  )
}