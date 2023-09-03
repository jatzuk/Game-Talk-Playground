package dev.jatzuk.gametalk.engine.gameloop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.withFrameNanos
import dev.jatzuk.gametalk.engine.telemetry.TelemetryRepository
import dev.jatzuk.gametalk.engine.util.timeElapsed

class ComposeGameLoop(
  private val telemetryRepository: TelemetryRepository,
  private val update: (Double) -> Unit
) {

  private var isRunning = false

  val deltaState = mutableFloatStateOf(0F)
  private var secondAccumulator = 0L
  private var lastTick = timeElapsed()
  private var fps = 0

  @Composable
  fun Start() {
    isRunning = true
    DisplayRefreshRateSyncGameLoop()
  }

  @Composable
  fun DisplayRefreshRateSyncGameLoop() {
    LaunchedEffect("ComposeGameLoopIsReal") {
      while (isRunning) {
        withFrameNanos { tickTime ->
          val delta = tickTime - lastTick
          lastTick = tickTime
          secondAccumulator += delta
          fps++

          val deltaDouble = delta / 1e9
          deltaState.value += deltaDouble.toFloat()
          update(deltaDouble)

          if (secondAccumulator >= 1_000_000_000L) {
            telemetryRepository.secondPass(fps, fps)
            fps = 0
            secondAccumulator = 0L
          }
        }
      }
    }
  }

  fun stop() {
    isRunning = false
  }
}