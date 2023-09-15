package dev.jatzuk.gametalk.playground.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.jatzuk.gametalk.R.string
import dev.jatzuk.gametalk.engine.util.safeInt
import dev.jatzuk.gametalk.playground.entrypoint.compose.ComposeEntryPoint
import dev.jatzuk.gametalk.playground.ui.RenderMode.COMPOSABLE
import dev.jatzuk.gametalk.playground.ui.RenderMode.COMPOSE_CANVAS
import dev.jatzuk.gametalk.playground.ui.RenderMode.COMPOSE_DRAW_SCOPE
import dev.jatzuk.gametalk.playground.ui.RenderMode.SURFACE
import dev.jatzuk.gametalk.playground.ui.RenderMode.SURFACE_SLEEP
import dev.jatzuk.gametalk.playground.ui.RenderMode.VIEW
import dev.jatzuk.gametalk.playground.ui.Routing.ComposeRoot
import dev.jatzuk.gametalk.playground.ui.Routing.Home
import dev.jatzuk.gametalk.playground.ui.Routing.ViewRoot
import dev.jatzuk.gametalk.playground.ui.component.HomeScreen
import dev.jatzuk.gametalk.playground.ui.component.ViewContainer
import dev.jatzuk.gametalk.playground.ui.theme.GameTalkPink
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTalkAppEntryPoint(viewModel: GameTalkViewModel) {
  val navController = rememberNavController()
  val context = LocalContext.current
  var topBarTitle by remember { mutableStateOf(context.getString(string.app_name)) }
  var topBarBackButtonVisibility by remember { mutableStateOf(false) }

  LaunchedEffect(navController) {
    navController.currentBackStackEntryFlow
      .collectLatest {
        topBarBackButtonVisibility = it.destination.route != Home.route

        val isHome = it.destination.route?.startsWith(Home.route) ?: false
        topBarTitle = if (isHome) {
          context.getString(string.app_name)
        } else {
          val selection = viewModel.selection.value
          val renderMode = selection.renderMode?.name?.lowercase().orEmpty()
          "${renderMode.capitalize(Locale.current)} / ${selection.scene}"
        }
      }
  }

  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          Text(
            text = topBarTitle,
            color = Color.White
          )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
          containerColor = GameTalkPink
        ),
        navigationIcon = {
          if (topBarBackButtonVisibility) {
            IconButton(onClick = { navController.navigateUp() }) {
              Icon(
                imageVector = Filled.ArrowBack,
                contentDescription = null
              )
            }
          }
        }
      )
    }
  ) { paddingValues ->
    NavHost(
      modifier = Modifier
        .padding(top = paddingValues.calculateTopPadding()),
      navController = navController,
      startDestination = Home.route,
    ) {
      composable(Home.route) { _ ->
        val selection = viewModel.selection.collectAsState()

        val renderModes = viewModel.renderModes.map { it.name }
          .map { it.lowercase().replaceFirstChar { it.uppercase() } }

        HomeScreen(
          scenes = viewModel.scenes.map { it.simpleName },
          selectedSceneItemIndex = viewModel.selectedSceneIndex,
          onSceneSelected = { viewModel.onSceneSelected(it) },

          renderTypes = renderModes,
          selectedRenderTypeIndex = viewModel.selectedRenderModeIndex,
          onRenderTypeSelected = { viewModel.onRendererSelected(it) },

          selectedGOSize = viewModel.stressTestScenesGOSize,
          onGOSizeChanged = { viewModel.onGoSizeChanged(it) },

          isTelemetryToggleChecked = selection.value.isTelemetryEnabled,
          onTelemetryToggleChecked = { viewModel.onTelemetryToggleChecked(it) },
          onClick = {
            val scene = selection.value.scene.orEmpty()
            val renderMode = checkNotNull(selection.value.renderMode)
            val goSize = selection.value.stressTestGOSize
            val isTelemetryEnabled = selection.value.isTelemetryEnabled

            val root = when (renderMode) {
              VIEW -> "viewRoot"
              SURFACE -> "viewRoot"
              SURFACE_SLEEP -> "viewRoot"
              COMPOSABLE -> "composeRoot"
              COMPOSE_DRAW_SCOPE -> "composeRoot"
              COMPOSE_CANVAS -> "composeRoot"
            }

            val mode = renderMode.name.lowercase()
            val navPath = "$root/$mode/$scene/${goSize}/${isTelemetryEnabled}"
            navController.navigate(navPath)
          }
        )
      }
      composable(
        ComposeRoot.route,
        arguments = listOf(navArgumentType, navArgumentScene, navArgumentTelemetryTracked)
      ) { backStackEntry ->
        val arguments = backStackEntry.arguments
        val type = arguments?.getString(argKeyType).orEmpty()
        val scene = arguments?.getString(argKeyScene).orEmpty()
        val goSize = arguments?.getString(argKeyGOSize).safeInt(1)
        val isTelemetryEnabled = arguments?.getBoolean(argKeyTelemetryEnabled) ?: true

        ComposeEntryPoint(RenderMode.valueOf(type.uppercase()), scene, goSize, isTelemetryEnabled)
      }
      composable(
        ViewRoot.route,
        arguments = listOf(navArgumentType, navArgumentScene)
      ) { backStackEntry ->
        val type = backStackEntry.arguments?.getString(argKeyType).orEmpty()
        ViewContainer(type)
      }
    }
  }
}