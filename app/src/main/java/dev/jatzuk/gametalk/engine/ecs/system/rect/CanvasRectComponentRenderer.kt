package dev.jatzuk.gametalk.engine.ecs.system.rect

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import dev.jatzuk.gametalk.engine.ecs.component.ComponentRenderer
import dev.jatzuk.gametalk.engine.ecs.component.rect.RectComponent
import dev.jatzuk.gametalk.engine.util.asRectFPosition

class CanvasRectComponentRenderer(
  fillColor: Int
) : ComponentRenderer<RectComponent, Canvas> {

  private val rect = RectF()
  private val paint = Paint().apply {
    color = fillColor
  }

  override fun render(component: RectComponent, target: Canvas) {
    rect.set(component.gameObject.transform.asRectFPosition())
    target.drawRect(rect, paint)
  }
}