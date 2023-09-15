package dev.jatzuk.gametalk.engine.ecs.system.sprite

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import dev.jatzuk.gametalk.engine.ecs.component.ComponentRenderer
import dev.jatzuk.gametalk.engine.ecs.component.sprite.SpriteComponent

class CanvasSpriteRenderer : ComponentRenderer<SpriteComponent, Canvas> {

  private val matrix = Matrix()
  private val alphaPaint = Paint()

  override fun render(component: SpriteComponent, target: Canvas) {
    val sprite = component.sprite

    matrix.reset()
    val transform = component.gameObject.transform

    val position = transform.position
    val rotation = transform.rotation
    val size = transform.size
    val scale = transform.scale

    val widthScale = size.width / sprite.width.toFloat()
    val heightScale = size.height / sprite.height.toFloat()

    matrix.postTranslate(-sprite.width / 2f, -sprite.height / 2f)
    matrix.postScale(scale.horizontal, scale.vertical)
    matrix.postScale(widthScale, heightScale)
    matrix.postRotate(rotation.degrees)
    matrix.postTranslate(position.x, position.y)

    target.drawBitmap(sprite.bitmap, matrix, alphaPaint)
  }
}