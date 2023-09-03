package dev.jatzuk.gametalk.engine.ecs.component.sprite

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asImageBitmap

class Sprite(
  val bitmap: Bitmap
) {

  val imageBitmap = bitmap.asImageBitmap()

  val width: Int  get() = bitmap.width
  val height: Int get() = bitmap.height
}