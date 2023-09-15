package dev.jatzuk.gametalk.engine.util

fun String?.safeInt(fallback: Int = 0): Int = this?.toIntOrNull() ?: fallback