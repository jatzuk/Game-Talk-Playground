package dev.jatzuk.gametalk.engine.scene

import android.content.Context
import android.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import dev.jatzuk.gametalk.engine.ecs.entity.GameObject

interface Scene {

  val isLoaded: Boolean
  val isAttached: Boolean
  val gameObjects: Iterable<GameObject>

  fun load(width: Int, height: Int, context: Context)
  fun unload()
  fun attach()
  fun detach()
  fun update(deltaTime: Double)
  fun render(canvas: Canvas)
  fun render(drawScope: DrawScope)
  fun render(drawScope: DrawScope, textMeasurer: TextMeasurer)
  fun addGameObject(gameObject: GameObject)
  fun deleteGameObject(gameObject: GameObject)
}
