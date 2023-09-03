package dev.jatzuk.gametalk.playground.entrypoint.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.ViewModelProvider
import dev.jatzuk.gametalk.engine.Engine
import dev.jatzuk.gametalk.engine.SceneConfigRepository
import dev.jatzuk.gametalk.engine.scene.Scene
import dev.jatzuk.gametalk.engine.telemetry.TelemetryRepository
import dev.jatzuk.gametalk.playground.ui.GameTalkActivity
import dev.jatzuk.gametalk.playground.ui.GameTalkViewModel
import dev.jatzuk.gametalk.playground.ui.SceneFactory

class GameView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

  private var telemetryRepository: TelemetryRepository? = null
  private var scene: Scene? = null
  private var engine: Engine? = null
  private var sceneFactory: SceneFactory? = null

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()

    val viewModel = ViewModelProvider(context as GameTalkActivity)[GameTalkViewModel::class.java]
    val selection = viewModel.selection.value
    val selectedScene = selection.scene ?: return
    val sceneConfigRepository = SceneConfigRepository(selection.stressTestGOSize, selection.isTelemetryEnabled)
    val telemetryRepository = TelemetryRepository().also { this.telemetryRepository = it }
    val sceneFactory = SceneFactory(sceneConfigRepository).also { sceneFactory = it }
    val scene = sceneFactory.create(selectedScene, telemetryRepository).also { scene = it }
    engine = Engine(scene, telemetryRepository)
  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
    scene?.load(width, height, context)
    scene?.attach()
    engine?.initViewRenderer()
    engine?.start()
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    engine?.update(canvas)
    postInvalidateOnAnimation()
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    engine?.stop()
    engine = null
    scene?.detach()
    scene?.unload()
    scene = null
    telemetryRepository = null
  }
}