package dev.jatzuk.gametalk.engine.gameloop

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.SurfaceHolder
import androidx.annotation.RequiresApi
import dev.jatzuk.gametalk.engine.telemetry.TelemetryRepository
import dev.jatzuk.gametalk.engine.util.timeElapsed
import dev.jatzuk.gametalk.engine.util.timeElapsedNanos
import dev.jatzuk.gametalk.playground.ui.RenderMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class SurfaceGameLoop(
  private val renderMode: RenderMode,
  private val surfaceHolder: SurfaceHolder,
  private val telemetryRepository: TelemetryRepository,
  private val onUpdate: (Double) -> Unit,
  private val onRender: (Canvas?) -> Unit
) {

  private val dispatcher = Executors.newSingleThreadExecutor {
    Thread(it, "SurfaceRenderThread")
  }.asCoroutineDispatcher()

  private val renderScope = CoroutineScope(dispatcher + SupervisorJob())

  private var isRunning = false
  private var gameLoopJob: Job? = null

  fun startGameLoop() {
    gameLoopJob = renderScope.launch {
      isRunning = true
      check(surfaceHolder.surface.isValid) { "Surface isn't valid" } // TODO: handle Exception
      when (renderMode) {
        RenderMode.SURFACE -> {
          gameLoopNoSleeps()
        }

        RenderMode.SURFACE_SLEEP -> {
          if (VERSION.SDK_INT >= VERSION_CODES.O) {
            gameLoopWithSleepsAndVariableUpsHWCanvas()
          } else {
            gameLoopWithSleepsAndVariableUps()
          }
        }

        else -> error("Unsupported render mode: $renderMode")
      }
    }
  }

  fun stopGameLoop() {
    isRunning = false
    gameLoopJob?.cancel()
    gameLoopJob = null
  }

  @SuppressLint("NewApi")
  private fun gameLoopNoSleeps() {
    var fps = 0
    var ups = 0

    val targetUps = 120
    val upsPeriod = 1_000 / targetUps

    val updateThreshold = upsPeriod * 1e6.toLong() // nanos in millisecond

    var secondCounter = 0.0
    var timeAccumulator = 0L
    var lastTickTime: Long = timeElapsedNanos() // SystemClock.elapsedRealtimeNanos()

    var canvas: Canvas?

    while (isRunning) {
      val currentTime = timeElapsedNanos()
      val deltaTime = currentTime - lastTickTime
      lastTickTime = currentTime

      timeAccumulator += deltaTime
      secondCounter += deltaTime

      while (timeAccumulator >= updateThreshold) {
        onUpdate(updateThreshold / 1e9)
        ups++
        timeAccumulator -= updateThreshold
      }

      try {
        // surfaceHolder.lockCanvas() - cpu only, very slow
        canvas = surfaceHolder.lockHardwareCanvas()
        onRender(canvas)
        fps++
        surfaceHolder.unlockCanvasAndPost(canvas)
      } catch (e: IllegalStateException) {
        stopGameLoop()
      }

      if (secondCounter >= 1e9) {
        telemetryRepository.secondPass(fps, ups)
        secondCounter = 0.0
        fps = 0
        ups = 0
      }
    }
  }

  @RequiresApi(VERSION_CODES.O)
  private fun gameLoopWithSleepsAndVariableUpsHWCanvas() {
    var canvas: Canvas?

    val maxUps = 120
    val upsPeriod = 1_000 / maxUps

    var fps = 0
    var ups = 0

    var seconds = 0

    var secondStartTimestamp = timeElapsed() // milliseconds
    var timeFromLastSecond: Long
    var sleepTime: Long
    var lastTickTime = timeElapsed()

    while (isRunning) {
      val now = timeElapsed()
      val frameDelta = now - lastTickTime
      lastTickTime = now

      timeFromLastSecond = now - secondStartTimestamp

      onUpdate(frameDelta / 1_000.0)
      ups++

      try {
        canvas = surfaceHolder.lockHardwareCanvas() // try surfaceHolder.lockCanvas() to see suffering
        onRender(canvas)
        fps++
        surfaceHolder.unlockCanvasAndPost(canvas)
      } catch (e: IllegalStateException) {
//        TODO("handle exception")
        stopGameLoop()
      }

      sleepTime = ups * upsPeriod - timeFromLastSecond
      if (sleepTime > 0) {
        try {
          Thread.sleep(sleepTime)
        } catch (e: InterruptedException) {
          stopGameLoop()
          TODO("handle exception")
        }
      }

      while (isRunning && sleepTime < 0 && ups < maxUps - 1) {
        onUpdate(1_000 / maxUps / 1_000.0)
        ups++
        timeFromLastSecond = timeElapsed() - secondStartTimestamp
        sleepTime = ups * upsPeriod - timeFromLastSecond
      }

      if (timeFromLastSecond >= 1_000) {
        seconds++
        telemetryRepository.secondPass(fps, ups)
        fps = 0
        ups = 0
        secondStartTimestamp = timeElapsed()
      }
    }
  }

  /**
   * totally the same as [gameLoopWithSleepsAndVariableUpsHWCanvas] but uses a cpu for drawing onto canvas
   * */
  private fun gameLoopWithSleepsAndVariableUps() {
    var canvas: Canvas?

    val maxUps = 120
    val upsPeriod = 1_000 / maxUps

    var fps = 0
    var ups = 0

    var seconds = 0

    var secondStartTimestamp = timeElapsed() // milliseconds
    var timeFromLastSecond: Long
    var sleepTime: Long
    var lastTickTime = timeElapsed()

    while (isRunning) {
      val now = timeElapsed()
      val frameDelta = now - lastTickTime
      lastTickTime = now

      timeFromLastSecond = now - secondStartTimestamp

      onUpdate(frameDelta / 1_000.0)
      ups++

      try {
        canvas = surfaceHolder.lockCanvas()
        onRender(canvas)
        fps++
        surfaceHolder.unlockCanvasAndPost(canvas)
      } catch (e: IllegalStateException) {
//        TODO("handle exception")
        stopGameLoop()
      }

      sleepTime = ups * upsPeriod - timeFromLastSecond
      if (sleepTime > 0) {
        try {
          Thread.sleep(sleepTime)
        } catch (e: InterruptedException) {
          stopGameLoop()
          TODO("handle exception")
        }
      }

      while (isRunning && sleepTime < 0 && ups < maxUps - 1) {
        onUpdate(1_000 / maxUps / 1_000.0)
        ups++
        timeFromLastSecond = timeElapsed() - secondStartTimestamp
        sleepTime = ups * upsPeriod - timeFromLastSecond
      }

      if (timeFromLastSecond >= 1_000) {
        seconds++
        telemetryRepository.secondPass(fps, ups)
        fps = 0
        ups = 0
        secondStartTimestamp = timeElapsed()
      }
    }
  }
}
