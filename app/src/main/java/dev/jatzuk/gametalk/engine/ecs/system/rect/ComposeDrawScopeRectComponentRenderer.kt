package dev.jatzuk.gametalk.engine.ecs.system.rect

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import dev.jatzuk.gametalk.engine.ecs.component.ComponentRenderer
import dev.jatzuk.gametalk.engine.ecs.component.rect.RectComponent
import dev.jatzuk.gametalk.engine.util.asComposeOffset
import dev.jatzuk.gametalk.engine.util.asComposeSize

class ComposeDrawScopeRectComponentRenderer(
  private val fillColor: Color
) : ComponentRenderer<RectComponent, DrawScope> {

  override fun render(component: RectComponent, target: DrawScope) {
    target.drawRect(
      size = component.gameObject.transform.asComposeSize(),
      topLeft = component.gameObject.transform.asComposeOffset(),
      color = fillColor
    )
  }
}