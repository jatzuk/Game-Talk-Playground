package dev.jatzuk.gametalk.engine.scripting

import dev.jatzuk.gametalk.engine.ecs.component.transform.Transform
import dev.jatzuk.gametalk.engine.ecs.entity.GameObject
import kotlin.random.Random

class RectGameObject(
  transform: Transform,
  private val screenWidth: Int,
  private val screenHeight: Int,
  val text: String
) : GameObject(transform) {

  private var moveSpeed = getMoveSpeed()

  override val id: Long = text.hashCode().toLong()

  override fun onUpdate(deltaTime: Double) {
    val offset = (moveSpeed * deltaTime).toFloat()
    val (_, y) = transform.offset(0f, -offset)

    if (y < -screenHeight) {
      moveSpeed = getMoveSpeed()
      val widthOffset = (transform.size.width / 2).toInt()
      val heightOffset = (transform.size.height / 2).toInt()
      transform.moveTo(
        Random.nextInt(-screenWidth / 2 - widthOffset, screenWidth / 2 + heightOffset).toFloat(),
        screenHeight / 2f + heightOffset
      )
    }
  }

  private fun getMoveSpeed(): Float = (Random.nextFloat() + 1) * 100f
}