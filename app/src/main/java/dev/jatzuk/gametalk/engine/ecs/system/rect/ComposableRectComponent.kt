package dev.jatzuk.gametalk.engine.ecs.system.rect

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.jatzuk.gametalk.engine.ecs.component.rect.RectComponent
import dev.jatzuk.gametalk.engine.util.fromRawPxToDP
import dev.jatzuk.gametalk.engine.util.toOffsetDp

@Composable
fun ComposableRectComponent(
  component: RectComponent,
  fillColor: Color,
) {

  val transform = component.gameObject.transform
  val (x, y) = transform.toOffsetDp()

  Box(
    modifier = Modifier
      .offset(x, y)
      .size(transform.fromRawPxToDP())
      .background(fillColor)
  )
}