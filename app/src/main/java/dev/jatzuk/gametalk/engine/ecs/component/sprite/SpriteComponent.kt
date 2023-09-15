package dev.jatzuk.gametalk.engine.ecs.component.sprite

import androidx.compose.runtime.Composable
import dev.jatzuk.gametalk.engine.ecs.component.Component
import dev.jatzuk.gametalk.engine.ecs.entity.GameObject
import dev.jatzuk.gametalk.engine.ecs.system.sprite.ComposableSpriteComponent

class SpriteComponent(
  val sprite: Sprite,
  override val gameObject: GameObject
) : Component {

  @Composable
  fun Render() {
    ComposableSpriteComponent(this)
  }
}