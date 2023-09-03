package dev.jatzuk.gametalk.engine.ecs.component.transform

import android.graphics.PointF

class Position(x: Float, y: Float) {

  private val point = PointF(x, y)

  val x: Float get() = point.x
  val y: Float get() = point.y

  fun offset(dx: Float, dy: Float) {
    point.offset(dx, dy)
  }

  fun moveTo(x: Float, y: Float) {
    point.set(x, y)
  }
}