package dev.jatzuk.gametalk.engine.scene

import android.content.Context
import android.graphics.Canvas
import androidx.annotation.CallSuper
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import dev.jatzuk.gametalk.engine.SceneConfigRepository
import dev.jatzuk.gametalk.engine.ecs.entity.GameObject
import dev.jatzuk.gametalk.engine.pipeline.EngineProcessingPipeline
import dev.jatzuk.gametalk.engine.telemetry.TelemetryRepository

abstract class AbstractScene(
  protected val sceneConfigRepository: SceneConfigRepository,
  private val telemetryRepository: TelemetryRepository
) : Scene {

  private var processingPipeline: EngineProcessingPipeline? = null

  override var isLoaded = false
  override var isAttached = false
  override val gameObjects = mutableSetOf<GameObject>()

  @CallSuper
  override fun load(width: Int, height: Int, context: Context) {
    processingPipeline = EngineProcessingPipeline(telemetryRepository, sceneConfigRepository)
    isLoaded = true
  }

  override fun unload() {
    gameObjects.clear()
    processingPipeline = null
    isLoaded = false
  }

  @CallSuper
  override fun attach() {
    check(isLoaded) { "You should call load() before attaching a scene" }
    isAttached = true
  }

  override fun detach() {
    isAttached = false
  }

  override fun update(deltaTime: Double) {
    check(isAttached) { "Scene is not attached" }
    processingPipeline?.update(gameObjects, deltaTime)
  }

  override fun render(canvas: Canvas) {
    check(isAttached) { "Scene is not attached" }
    processingPipeline?.renderCanvas(gameObjects, canvas)
  }

  override fun render(drawScope: DrawScope) {
    check(isAttached) { "Scene is not attached" }
    processingPipeline?.composeRenderCanvas(gameObjects, drawScope)
  }

  override fun render(drawScope: DrawScope, textMeasurer: TextMeasurer) {
    check(isAttached) { "Scene is not attached" }
    processingPipeline?.composeRenderDrawScope(gameObjects, drawScope, textMeasurer)
  }

  @Composable
  fun Render() {
    check(isAttached) { "Scene is not attached" }
    processingPipeline?.ComposeRenderComposable(gameObjects)
  }

  override fun addGameObject(gameObject: GameObject) {
    check(isLoaded) { "Scene is not attached" }
    gameObjects.add(gameObject)
  }

  override fun deleteGameObject(gameObject: GameObject) {
    check(isLoaded) { "Scene is not attached" }
    gameObjects.remove(gameObject)
  }
}