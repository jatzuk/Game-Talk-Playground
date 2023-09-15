package dev.jatzuk.gametalk.engine.telemetry

data class Telemetry(
  val gameObjectsCount: Int,
  val fps: Int,
  val ups: Int,
  val avgRenderPass: Long,
  val avgUpdatePass: Long,
  val lastRenderPass: Double,
  val lastUpdatePass: Double,
  val maxRenderPass: Long,
  val maxUpdatePass: Long,
  val fullPass: Float
)