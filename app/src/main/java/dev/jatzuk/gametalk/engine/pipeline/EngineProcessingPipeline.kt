package dev.jatzuk.gametalk.engine.pipeline

import android.graphics.Canvas
import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.unit.dp
import dev.jatzuk.gametalk.engine.SceneConfigRepository
import dev.jatzuk.gametalk.engine.ecs.entity.GameObject
import dev.jatzuk.gametalk.engine.ecs.system.telemetry.CanvasTelemetryRenderer
import dev.jatzuk.gametalk.engine.ecs.system.telemetry.ComposableTelemetryRenderer
import dev.jatzuk.gametalk.engine.ecs.system.telemetry.ComposeDrawScopeTelemetryRenderer
import dev.jatzuk.gametalk.engine.renderer.CanvasPipelineRenderer
import dev.jatzuk.gametalk.engine.renderer.ComposeDrawScopePipelineRenderer
import dev.jatzuk.gametalk.engine.telemetry.Telemetry
import dev.jatzuk.gametalk.engine.telemetry.TelemetryRepository
import dev.jatzuk.gametalk.engine.util.center
import kotlin.system.measureNanoTime

class EngineProcessingPipeline(
  private val telemetryRepository: TelemetryRepository,
  private val sceneConfigRepository: SceneConfigRepository
) {

  private val nativeCanvasRenderer = CanvasPipelineRenderer()
  private val composePipelineRenderer = ComposeDrawScopePipelineRenderer()

  private val canvasTelemetryRenderer = CanvasTelemetryRenderer()
  private val composeTelemetryRendererSystem = ComposeDrawScopeTelemetryRenderer()

  fun update(gameObjects: Collection<GameObject>, deltaTime: Double) {
    updateInternal(
      block = { gameObjects.forEach { it.onUpdate(deltaTime) } },
      function = { telemetryRepository.updateTime(it) }
    )
  }

  fun renderCanvas(gameObjects: Collection<GameObject>, canvas: Canvas) {
    renderInternal(
      gameObjectsSize = gameObjects.size,
      block = {
        canvas.drawColor(Color.WHITE)
        val (x, y) = canvas.center()
        canvas.translate(x, y)

        gameObjects.forEach {
          nativeCanvasRenderer.renderCanvas(it, canvas)
        }
      },
      telemetryRendering = {
        canvasTelemetryRenderer.render(it, canvas)
      }
    )
  }

  fun composeRenderCanvas(gameObjects: Collection<GameObject>, drawScope: DrawScope) {
    renderCanvas(gameObjects, drawScope.drawContext.canvas.nativeCanvas)
  }

  fun composeRenderDrawScope(
    gameObjects: Collection<GameObject>,
    drawScope: DrawScope,
    textMeasurer: TextMeasurer
  ) {
    renderInternal(
      gameObjectsSize = gameObjects.size,
      block = {
        drawScope.translate(drawScope.center.x, drawScope.center.y) {
          drawContext.canvas.nativeCanvas.drawColor(Color.WHITE)

          gameObjects.forEach {
            composePipelineRenderer.renderDrawScope(it, this)
          }
        }
      },
      telemetryRendering = {
        composeTelemetryRendererSystem.render(it, drawScope, textMeasurer)
      }
    )
  }

  @Composable
  fun ComposeRenderComposable(gameObjects: Collection<GameObject>) {
    renderInternal(
      gameObjectsSize = gameObjects.size,
      block = {
        val (width, height) = with(LocalConfiguration.current) {
          screenWidthDp / 2 to screenHeightDp / 2
        }

        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(color = androidx.compose.ui.graphics.Color.White)
            .offset(width.dp, height.dp)
        ) {
          gameObjects.forEach {
            composePipelineRenderer.RenderComposable(it)
          }
        }
      },
      telemetryRendering = {
        ComposableTelemetryRenderer(it)
      }
    )
  }

  private inline fun updateInternal(block: () -> Unit, function: Function1<Long, Unit>) {
    if (sceneConfigRepository.isTelemetryEnabled) {
      val timeTaken = measureNanoTime(block)
      function(timeTaken)
    } else {
      block()
    }
  }

  private inline fun renderInternal(
    gameObjectsSize: Int,
    block: () -> Unit,
    telemetryRendering: (Telemetry) -> Unit
  ) {
    if (sceneConfigRepository.isTelemetryEnabled) {
      val timeTaken = measureNanoTime(block)

      telemetryRepository.renderTime(timeTaken)
      val telemetry = telemetryRepository.telemetry(gameObjectsSize)
      telemetryRendering(telemetry)
    } else {
      block()
    }
  }
}