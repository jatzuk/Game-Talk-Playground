package dev.jatzuk.gametalk.engine.ecs.component.rect

import android.content.Context
import android.graphics.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.drawscope.DrawScope
import dev.jatzuk.gametalk.R
import dev.jatzuk.gametalk.engine.ecs.component.Component
import dev.jatzuk.gametalk.engine.ecs.entity.GameObject
import dev.jatzuk.gametalk.engine.ecs.system.rect.CanvasRectComponentRenderer
import dev.jatzuk.gametalk.engine.ecs.system.rect.ComposableRectComponent
import dev.jatzuk.gametalk.engine.ecs.system.rect.ComposeDrawScopeRectComponentRenderer
import dev.jatzuk.gametalk.playground.ui.theme.GameTalkPink

class RectComponent(
  override val gameObject: GameObject,
  context: Context
) : Component {

  private val canvasRenderer = CanvasRectComponentRenderer(context.getColor(R.color.pink))
  private val composeRenderer = ComposeDrawScopeRectComponentRenderer(GameTalkPink)

  fun render(canvas: Canvas) {
    canvasRenderer.render(this, canvas)
  }

  fun render(drawScope: DrawScope) {
    composeRenderer.render(this, drawScope)
  }

  @Composable
  fun Render() {
    ComposableRectComponent(this, GameTalkPink)
  }
}