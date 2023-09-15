package dev.jatzuk.gametalk.engine.ecs.component.transform

class Transform(
  val position: Position,
  val rotation: Rotation,
  val size: Size,
  val scale: Scale
) {

  fun offset(dx: Float, dy: Float): Pair<Float, Float> {
    position.offset(dx, dy)
    return position.x to position.y
  }

  fun moveTo(x: Float, y: Float): Pair<Float, Float> {
    position.moveTo(x, y)
    return position.x to position.y
  }
}

class Rotation(var degrees: Float)
class Size(var width: Float, var height: Float = width)
class Scale(var horizontal: Float, var vertical: Float = horizontal)
