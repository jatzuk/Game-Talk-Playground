package dev.jatzuk.gametalk.engine.ecs.system.telemetry

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import dev.jatzuk.gametalk.engine.telemetry.Telemetry

class CanvasTelemetryRenderer {

  private val paint = Paint().apply {
    color = Color.BLACK
    textSize = 40f
    isAntiAlias = true
  }

  private var y = 200f
    get() {
      field += 60
      return field
    }

  fun render(component: Telemetry, canvas: Canvas) {
    canvas.drawDebugInfo(component)
  }

  private fun Canvas.drawDebugInfo(telemetry: Telemetry) {
    val x = -width / 2f
    y = -height / 2f

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

    drawText("GO's: $gameObjectsCount", x, y, paint)
    drawText("fps: $fps", x, y, paint)
    drawText("ups: $ups", x, y, paint)
    drawText("avgR: $avgRender", x, y, paint)
    drawText("avgU: $avgUpdate", x, y, paint)
    drawText("lastR: $lastRender", x, y, paint)
    drawText("lastU: $lastUpdate", x, y, paint)
    drawText("maxR: $maxRender", x, y, paint)
    drawText("maxU: $maxUpdate", x, y, paint)
    drawText("full: $fullPass ms.", x, y, paint)
  }
}