package dev.jatzuk.gametalk.engine.util

import android.graphics.RectF
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.jatzuk.gametalk.engine.ecs.component.transform.Transform

fun Transform.asRectFPosition(): RectF {
  val (width, height) = with(size) { width to height }
  return RectF(
    position.x - width / 2,
    position.y - height / 2,
    position.x + width / 2,
    position.y + height / 2
  )
}

fun Transform.asComposeSize(): Size = Size(size.width, size.height)

fun Transform.asComposeOffset(): Offset = Offset(
  position.x - size.width / 2,
  position.y - size.height / 2
)

@Composable
fun Transform.fromRawPxToDP(): DpSize {
  return with(LocalDensity.current) {
    val width = (size.width / density).dp
    val height = (size.height / density).dp
    DpSize(width, height)
  }
}

@Composable
fun Transform.toOffsetDp(): Pair<Dp, Dp> {
  val halfWidth = size.width / 2
  val halfHeight = size.height / 2
  val (x, y) = with(LocalDensity.current) {
    (position.x - halfWidth) / density to (position.y - halfHeight) / density
  }
  return x.dp to y.dp
}