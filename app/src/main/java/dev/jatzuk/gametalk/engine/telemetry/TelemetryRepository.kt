package dev.jatzuk.gametalk.engine.telemetry

import kotlin.math.max

class TelemetryRepository {

  var seconds = 0
    private set

  var fps = 0
    private set

  var ups = 0
    private set

  private var updateTimeAccumulator = 0L
  private var renderTimeAccumulator = 0L

  var lastRenderTimeNanos = 0L
    private set
  val lastRenderTimeMillis: Double get() = lastRenderTimeNanos / 1e6

  var lastUpdateTimeNanos = 0L
    private set
  val lastUpdateTimeMillis: Double get() = lastUpdateTimeNanos / 1e6

  var avgUpdateTimeMillis = 0L
    private set
  var avgRenderTimeMillis = 0L
    private set

  var maxUpdateTimeNanos = 0L
    private set
  val maxUpdateTimeMillis: Long get() = maxUpdateTimeNanos / 1e6.toLong()

  var maxRenderTimeNanos = 0L
    private set
  val maxRenderTimeMillis: Long get() = maxRenderTimeNanos / 1e6.toLong()

  fun secondPass(frames: Int, updates: Int) {
    fps = frames
    ups = updates
    seconds++
  }

  fun updateTime(nanos: Long) {
    this.updateTimeAccumulator += nanos
    lastUpdateTimeNanos = nanos

    if (updateTimeAccumulator >= 1_000_000) {
      avgUpdateTimeMillis = updateTimeAccumulator / ups.coerceAtLeast(1) / 1_000
      updateTimeAccumulator = 0L
    }

    if (seconds > 1) {
      maxUpdateTimeNanos = max(nanos, maxUpdateTimeNanos)
    }
  }

  fun renderTime(nanos: Long) {
    renderTimeAccumulator += nanos
    lastRenderTimeNanos = nanos

    if (renderTimeAccumulator >= 1_000_000) {
      avgRenderTimeMillis = renderTimeAccumulator / fps.coerceAtLeast(1) / 1_000
      renderTimeAccumulator = 0L
    }

    if (seconds > 1) {
      maxRenderTimeNanos = max(nanos, maxRenderTimeNanos)
    }
  }

  fun telemetry(gameObjSize: Int): Telemetry {
    val fullPass = lastRenderTimeMillis + lastUpdateTimeMillis
    return Telemetry(
      gameObjectsCount = gameObjSize,
      fps = fps,
      ups = ups,
      avgRenderPass = avgRenderTimeMillis,
      avgUpdatePass = avgUpdateTimeMillis,
      lastRenderPass = lastRenderTimeMillis,
      lastUpdatePass = lastUpdateTimeMillis,
      maxRenderPass = maxRenderTimeMillis,
      maxUpdatePass = maxUpdateTimeMillis,
      fullPass = fullPass.toFloat()
    )
  }
}