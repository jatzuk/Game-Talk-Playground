package dev.jatzuk.gametalk.engine.util

import android.graphics.Canvas

fun Canvas.center(): Pair<Float, Float> {
  return width / 2f to height / 2f
}