package dev.jatzuk.gametalk.engine.ecs.system.telemetry

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import dev.jatzuk.gametalk.engine.telemetry.Telemetry

class ComposeDrawScopeTelemetryRenderer {

  private var y = 0f
    get() {
      field += 24.sp.value * 2
      return field
    }

  private val textStyle = TextStyle(
    fontSize = 16.sp
  )

  /**
   * Text rendering is very heavy lifting operation. Try to avoid it as possible.
   * */
  fun render(telemetry: Telemetry, drawScope: DrawScope, textMeasurer: TextMeasurer) {
    val x = -drawScope.center.x
    y = -drawScope.center.y

    val gameObjectsCount = telemetry.gameObjectsCount
    val ups = telemetry.ups
    val fps = telemetry.fps
    val avgRender = telemetry.avgRenderPass
    val avgUpdate = telemetry.avgUpdatePass
    val lastRender = telemetry.lastRenderPass
    val lastUpdate = telemetry.lastUpdatePass
    val maxRender = telemetry.maxRenderPass
    val maxUpdate = telemetry.maxUpdatePass
    val fullPass = telemetry.fullPass

    drawScope.translate(drawScope.center.x, drawScope.center.y) {
      drawText("GO's: $gameObjectsCount", textMeasurer, x, -drawScope.center.y)
      drawText("fps: $fps", textMeasurer, x)
      drawText("ups: $ups", textMeasurer, x)
      drawText("avgR: $avgRender", textMeasurer, x)
      drawText("avgU: $avgUpdate", textMeasurer, x)
      drawText("lastR: $lastRender", textMeasurer, x)
      drawText("lastU: $lastUpdate", textMeasurer, x)
      drawText("maxR: $maxRender", textMeasurer, x)
      drawText("maxU: $maxUpdate", textMeasurer, x)
      drawText("full: $fullPass ms.", textMeasurer, x)
    }
  }

  private fun DrawScope.drawText(
    text: String,
    textMeasurer: TextMeasurer,
    x: Float,
    y: Float = this@ComposeDrawScopeTelemetryRenderer.y
  ) {
    drawText(
      text = text,
      textMeasurer = textMeasurer,
      topLeft = Offset(x, y),
      style = textStyle
    )
  }
}