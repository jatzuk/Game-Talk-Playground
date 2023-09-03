package dev.jatzuk.gametalk.playground.entrypoint.surface

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.lifecycle.ViewModelProvider
import dev.jatzuk.gametalk.engine.Engine
import dev.jatzuk.gametalk.engine.SceneConfigRepository
import dev.jatzuk.gametalk.engine.scene.Scene
import dev.jatzuk.gametalk.engine.telemetry.TelemetryRepository
import dev.jatzuk.gametalk.playground.ui.GameTalkActivity
import dev.jatzuk.gametalk.playground.ui.GameTalkViewModel
import dev.jatzuk.gametalk.playground.ui.SceneFactory

class GameSurfaceView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr),
  SurfaceHolder.Callback,
  SurfaceHolder.Callback2 {

  private var telemetryRepository: TelemetryRepository? = null
  private var scene: Scene? = null
  private var engine: Engine? = null
  private var sceneFactory: SceneFactory? = null

  init {
    holder.addCallback(this)
  }

  override fun surfaceCreated(holder: SurfaceHolder) {
    val viewModel = ViewModelProvider(context as GameTalkActivity).get(GameTalkViewModel::class.java)
    val selection = viewModel.selection.value
    val selectedScene = selection.scene ?: return
    val mode = selection.renderMode ?: return
    val sceneConfigRepository = SceneConfigRepository(selection.stressTestGOSize, selection.isTelemetryEnabled)
    val telemetryRepository = TelemetryRepository().also { this.telemetryRepository = it }
    val sceneFactory = SceneFactory(sceneConfigRepository).also { sceneFactory = it }
    val scene = sceneFactory.create(selectedScene, telemetryRepository).also { scene = it }
    engine = Engine(scene, telemetryRepository)
    engine?.initSurfaceRenderer(holder, mode)
  }

  override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    scene?.load(width, height, context)
    scene?.attach()
    engine?.start()
  }

  override fun surfaceDestroyed(holder: SurfaceHolder) {
    engine?.stop()
    engine = null
    scene?.unload()
    scene = null
    sceneFactory = null
    telemetryRepository = null
  }

  override fun surfaceRedrawNeeded(holder: SurfaceHolder) = Unit
}