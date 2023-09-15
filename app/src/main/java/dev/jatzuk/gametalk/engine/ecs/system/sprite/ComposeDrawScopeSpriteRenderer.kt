package dev.jatzuk.gametalk.engine.ecs.system.sprite

import androidx.compose.ui.graphics.drawscope.DrawScope
import dev.jatzuk.gametalk.engine.ecs.component.ComponentRenderer
import dev.jatzuk.gametalk.engine.ecs.component.sprite.SpriteComponent
import dev.jatzuk.gametalk.engine.util.asComposeOffset

class ComposeDrawScopeSpriteRenderer: ComponentRenderer<SpriteComponent, DrawScope> {

  override fun render(component: SpriteComponent, target: DrawScope) {
    target.drawImage(
      image = component.sprite.imageBitmap,
      topLeft = component.gameObject.transform.asComposeOffset(),
    )
  }
}