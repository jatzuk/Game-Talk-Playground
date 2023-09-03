package dev.jatzuk.gametalk.playground.entrypoint.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import dev.jatzuk.gametalk.engine.Engine
import dev.jatzuk.gametalk.engine.SceneConfigRepository
import dev.jatzuk.gametalk.engine.telemetry.TelemetryRepository
import dev.jatzuk.gametalk.playground.entrypoint.compose.ComposeRenderMode.COMPOSABLE
import dev.jatzuk.gametalk.playground.entrypoint.compose.ComposeRenderMode.DRAW_SCOPE
import dev.jatzuk.gametalk.playground.entrypoint.compose.ComposeRenderMode.NATIVE_CANVAS
import dev.jatzuk.gametalk.playground.ui.RenderMode
import dev.jatzuk.gametalk.playground.ui.SceneFactory
import kotlin.math.roundToInt

@Composable
fun ComposeEntryPoint(
  mode: RenderMode,
  sceneName: String,
  stressTestGOSize: Int,
  isTelemetryEnabled: Boolean
) {
  val density = LocalDensity.current.density
  val (width, height) = with(LocalConfiguration.current) {
    screenWidthDp * density to screenHeightDp * density
  }

  val sceneConfigRepository = remember {
    SceneConfigRepository(stressTestGOSize, isTelemetryEnabled)
  }
  val telemetryRepository = remember { TelemetryRepository() }
  val sceneFactory = remember { SceneFactory(sceneConfigRepository) }
  val scene = remember { sceneFactory.create(sceneName, telemetryRepository) }
  val engine = remember { Engine(scene, telemetryRepository) }

  var isInitialized by remember { mutableStateOf(false) }
  if (!isInitialized) {
    scene.load(width.roundToInt(), height.roundToInt(), LocalContext.current)
    scene.attach()
    engine.initComposeRenderer()
    isInitialized = true
  }

  val composeMode = when (mode) {
    RenderMode.COMPOSABLE -> COMPOSABLE
    RenderMode.COMPOSE_DRAW_SCOPE -> DRAW_SCOPE
    RenderMode.COMPOSE_CANVAS -> NATIVE_CANVAS
    else -> error("Unsupported mode: $mode")
  }

  Surface(
    modifier = Modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background
  ) {
    engine.SetupComposeRendering(composeMode)
  }
}

enum class ComposeRenderMode {
  NATIVE_CANVAS, // android view's canvas
  DRAW_SCOPE, // self api
  COMPOSABLE // building blocks like box, image etc
}