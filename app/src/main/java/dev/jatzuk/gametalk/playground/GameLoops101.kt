package dev.jatzuk.gametalk.playground

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameNanos
import dev.jatzuk.gametalk.engine.util.timeElapsed
import dev.jatzuk.gametalk.engine.util.timeElapsedNanos
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext
import kotlin.random.Random

private var isRunning = true
private const val delayMillis = 100L

private class Player(var x: Int)

private val player = Player(10)

private fun gameLoopFirst() {
  while (isRunning) {
    handleInput()
    update()
    render()
  }
}

private suspend fun gameLoopDelay() {
  while (isRunning) {
    coroutineContext.ensureActive()
    handleInput()
    update()
    render()
    delay(delayMillis)
  }
}

private fun gameLoopDelta() {
  var lastTickTime = SystemClock.elapsedRealtime()
  var now: Long
  var deltaTime: Long

  while (isRunning) {
    now = SystemClock.elapsedRealtime()
    deltaTime = now - lastTickTime
    lastTickTime = now

    handleInput()
    update(deltaTime / 1_000.0) // e.g. 0.016
    render()
  }
}

private fun gameLoopDeterministic() {
  var fps = 0
  var ups = 0

  val targetUps = 60
  val upsPeriod = 1_000 / targetUps
  val updateThreshold = upsPeriod * 1e6.toLong() // nanos in millisecond

  var secondCounter = 0.0
  var timeAccumulator = 0L
  var lastTickTime: Long = timeElapsedNanos() // SystemClock.elapsedRealtimeNanos()
  var currentTime: Long
  var deltaTime: Long

  while (isRunning) {
    currentTime = timeElapsedNanos()
    deltaTime = currentTime - lastTickTime
    lastTickTime = currentTime

    timeAccumulator += deltaTime
    secondCounter += deltaTime

    handleInput()

    while (timeAccumulator >= updateThreshold) {
      update(updateThreshold / 1e9)
      ups++
      timeAccumulator -= updateThreshold
    }

    render()
    fps++

    if (secondCounter >= 1e9) {
      // TODO: performance telemetry
      secondCounter = 0.0
      fps = 0
      ups = 0
    }
  }
}

private fun gameLoopSleep() {
  var fps = 0
  var ups = 0

  val targetUps = 60
  val upsPeriod = 1_000 / targetUps

  var now: Long
  var deltaTime: Long
  var lastSecondTimestamp: Long = timeElapsed() // milliseconds
  var lastTickTime: Long = timeElapsed()
  var lastSecondDelta: Long
  var sleepTime: Long

  while (isRunning) {
    now = timeElapsed()
    deltaTime = now - lastTickTime
    lastTickTime = now
    lastSecondDelta = now - lastSecondTimestamp

    handleInput()

    update(deltaTime / 1_000.0)
    ups++

    render()
    fps++

    sleepTime = ups * upsPeriod - lastSecondDelta
    if (sleepTime > 0) {
      Thread.sleep(sleepTime) // TODO: handle InterruptedException
    }

    while (isRunning && sleepTime < 0 && ups < targetUps - 1) {
      update(upsPeriod / 1_000.0)
      ups++
      lastSecondDelta = timeElapsed() - lastSecondTimestamp
      sleepTime = ups * upsPeriod - lastSecondDelta
    }

    if (lastSecondDelta >= 1_000) {
      // TODO: performance telemetry
      fps = 0
      ups = 0
      lastSecondTimestamp = timeElapsed()
    }
  }
}

private fun render() {
  Thread.sleep(Random.nextLong(3, 9))
}

private fun update() {
  player.x += 50 // move by 50 px each update call
}

private fun update(fl: Double) {
}

private fun handleInput() {
}

@Composable
private fun ComposableGameLoop() {
  LaunchedEffect("ComposeGameLoopIsReal") {
    while (isRunning) {
      withFrameNanos { tickTime ->
        // game loop here
      }
    }
  }
}