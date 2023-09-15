package dev.jatzuk.gametalk.engine.ecs.system.telemetry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import dev.jatzuk.gametalk.engine.telemetry.Telemetry

@Composable
fun ComposableTelemetryRenderer(telemetry: Telemetry) {
  Column(
    modifier = Modifier
      .fillMaxSize(),
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.Start
  ) {
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

    val fontSize = 16.sp
    
    Text(
      text = "GO's $gameObjectsCount",
      fontSize = fontSize,
      color = Color.Black
    )

    Text(
      text = "ups: $ups",
      fontSize = fontSize,
      color = Color.Black
    )

    Text(
      text = "fps: $fps",
      fontSize = fontSize,
      color = Color.Black
    )

    Text(
      text = "avgR: $avgRender",
      fontSize = fontSize,
      color = Color.Black
    )

    Text(
      text = "avgU: $avgUpdate",
      fontSize = fontSize,
      color = Color.Black
    )

    Text(
      text = "lastR: $lastRender",
      fontSize = fontSize,
      color = Color.Black
    )

    Text(
      text = "lastU: $lastUpdate",
      fontSize = fontSize,
      color = Color.Black
    )

    Text(
      text = "maxR: $maxRender",
      fontSize = fontSize,
      color = Color.Black
    )

    Text(
      text = "maxU: $maxUpdate",
      fontSize = fontSize,
      color = Color.Black
    )

    Text(
      text = "full: $fullPass ms.",
      fontSize = fontSize,
      color = Color.Black
    )
  }
}