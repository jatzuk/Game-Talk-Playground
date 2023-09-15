package dev.jatzuk.gametalk.playground.ui

import androidx.navigation.navArgument

val argKeyType = "type"
val argKeyScene = "scene"
val argKeyGOSize = "argGOSize"
val argKeyTelemetryEnabled = "telemetryEnabled"

val navArgumentType = navArgument(argKeyType) { defaultValue = "" }
val navArgumentScene = navArgument(argKeyScene) { defaultValue = "" }
val navArgumentGOSize = navArgument(argKeyGOSize) { defaultValue = { 1 }}
val navArgumentTelemetryTracked = navArgument(argKeyTelemetryEnabled) { defaultValue = true }

sealed class Routing(open val route: String) {

  object Home : Routing("home")
  object ComposeRoot : Routing("composeRoot") {

    override val route: String = "${super.route}/{$argKeyType}/{$argKeyScene}/{$argKeyGOSize}/{$argKeyTelemetryEnabled}"
  }

  object ViewRoot : Routing("viewRoot") {

    override val route: String = "${super.route}/{$argKeyType}/{$argKeyScene}/{$argKeyGOSize}/{$argKeyTelemetryEnabled}"
  }
}