package dev.jatzuk.gametalk.engine.ecs.system.sprite

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.jatzuk.gametalk.engine.ecs.component.sprite.SpriteComponent
import dev.jatzuk.gametalk.engine.util.toOffsetDp

@Composable
fun ComposableSpriteComponent(component: SpriteComponent) {
  val transform = component.gameObject.transform
  val (x, y) = transform.toOffsetDp()

  Image(
    modifier = Modifier
      .offset(x, y),
    bitmap = component.sprite.imageBitmap,
    contentDescription = null
  )
}
