package dev.jatzuk.gametalk.engine

import android.graphics.Canvas
import android.view.SurfaceHolder
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.rememberTextMeasurer
import dev.jatzuk.gametalk.engine.gameloop.ComposeGameLoop
import dev.jatzuk.gametalk.engine.gameloop.SurfaceGameLoop
import dev.jatzuk.gametalk.engine.gameloop.ViewGameLoop
import dev.jatzuk.gametalk.engine.scene.AbstractScene
import dev.jatzuk.gametalk.engine.telemetry.TelemetryRepository
import dev.jatzuk.gametalk.playground.entrypoint.compose.ComposeRenderMode
import dev.jatzuk.gametalk.playground.ui.RenderMode

class Engine(
  private val scene: AbstractScene,
  private val telemetryRepository: TelemetryRepository
) {

  private var surfaceRenderer: SurfaceGameLoop? = null
  private var viewGameLoop: ViewGameLoop? = null
  private var composeGameLoop: ComposeGameLoop? = null

  fun initComposeRenderer() {
    composeGameLoop = ComposeGameLoop(
      telemetryRepository,
      update = this::update
    )
  }

  fun initViewRenderer() {
    viewGameLoop = ViewGameLoop(telemetryRepository)
  }

  fun initSurfaceRenderer(surfaceHolder: SurfaceHolder, renderMode: RenderMode) {
    surfaceRenderer = SurfaceGameLoop(
      renderMode,
      surfaceHolder,
      telemetryRepository,
      onUpdate = this::update,
      onRender = ::renderCanvas
    )
  }

  fun start() {
    surfaceRenderer?.startGameLoop()
    viewGameLoop?.start()
  }

  @Composable
  fun SetupComposeNativeRendering() {
    SharedCanvas {
      scene.render(it.drawContext.canvas.nativeCanvas)
    }
  }

  @Composable
  fun SetupComposeDrawScopeRendering() {
    val textMeasurer = rememberTextMeasurer()

    SharedCanvas {
      scene.render(it, textMeasurer)
    }
  }

  @Composable
  fun SetupComposableRendering() {
    scene.Render()
  }

  @Composable
  fun SetupComposeRendering(renderMode: ComposeRenderMode) {
    composeGameLoop?.Start()

    // trigger recomposition
    // (don't to this, this is a bad approach used just for demonstration purposes)
    val totalDelta = composeGameLoop?.deltaState?.value ?: 0f

    when (renderMode) {
      ComposeRenderMode.NATIVE_CANVAS -> SetupComposeNativeRendering()
      ComposeRenderMode.DRAW_SCOPE -> SetupComposeDrawScopeRendering()
      ComposeRenderMode.COMPOSABLE -> SetupComposableRendering()
    }
  }

  fun stop() {
    surfaceRenderer?.stopGameLoop()
    viewGameLoop?.stop()
    composeGameLoop?.stop()
  }

  fun update(canvas: Canvas) {
    val renderer = viewGameLoop ?: return
    val delta = renderer.drawCall()
    update(delta)
    renderCanvas(canvas)
  }

  private fun update(dt: Double) {
    scene.update(dt)
  }

  private fun renderCanvas(canvas: Canvas?) {
    val nonNullCanvas = canvas ?: return
    scene.render(nonNullCanvas)
  }

  @Composable
  private fun SharedCanvas(onDraw: (DrawScope) -> Unit) {
    Canvas(
      modifier = Modifier
        .fillMaxSize(),
      onDraw = {
        onDraw(this)
      }
    )
  }
}