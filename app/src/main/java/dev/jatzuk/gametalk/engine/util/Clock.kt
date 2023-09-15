package dev.jatzuk.gametalk.engine.util

import android.os.SystemClock

fun timeElapsedNanos(): Long = SystemClock.elapsedRealtimeNanos()

fun timeElapsed(): Long = SystemClock.elapsedRealtime()