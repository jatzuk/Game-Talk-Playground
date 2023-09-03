package dev.jatzuk.gametalk.engine.gameloop

import dev.jatzuk.gametalk.engine.telemetry.TelemetryRepository
import dev.jatzuk.gametalk.engine.util.timeElapsedNanos

class ViewGameLoop(
  private val telemetryRepository: TelemetryRepository
) {

  private var lastTimestamp: Long = timeElapsedNanos()
  private var secondAccumulator = 0L

  private var fps = 0

  var isRunning = false
    private set

  fun start() {
    isRunning = true
  }

  fun stop() {
    isRunning = false
  }

  fun drawCall(): Double {
    val now = timeElapsedNanos()
    val delta = now - lastTimestamp
    lastTimestamp = now
    secondAccumulator += delta
    fps++

    if (secondAccumulator >= 1e9) {
      telemetryRepository.secondPass(fps, fps)
      fps = 0
      secondAccumulator = 0L
    }

    return delta / 1e9
  }
}