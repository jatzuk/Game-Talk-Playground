package dev.jatzuk.gametalk.playground.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlin.math.abs

class GameTalkViewModel : ViewModel() {

  val scenes get() = SceneFactory.scenes
  val renderModes = RenderMode.values()

  val selectedSceneIndex: Int
    get() {
      val selection = selection.value
      val current = selection.scene.orEmpty()

      val index = scenes
        .map { it.simpleName }
        .indexOf(current)
      return index.coerceAtLeast(0)
    }

  val selectedRenderModeIndex: Int
    get() {
      val selection = selection.value
      val current = selection.renderMode

      val index = renderModes.indexOf(current)
      return index.coerceAtLeast(0)
    }
  val stressTestScenesGOSize: Int
    get() {
      return selection.value.stressTestGOSize
    }

  private val _selection = MutableStateFlow(
    Selection(
      scene = scenes.first().simpleName,
      renderMode = renderModes.first(),
      stressTestGOSize = 100,
      isTelemetryEnabled = true
    )
  )
  val selection = _selection.asStateFlow()

  fun onSceneSelected(index: Int) {
    _selection.getAndUpdate {
      it.copy(
        scene = scenes[index].simpleName
      )
    }
  }

  fun onRendererSelected(index: Int) {
    _selection.getAndUpdate {
      it.copy(
        renderMode = renderModes[index]
      )
    }
  }

  fun onGoSizeChanged(size: Int) {
    _selection.getAndUpdate {
      it.copy(
        stressTestGOSize = size
      )
    }
  }

  fun onTelemetryToggleChecked(isEnabled: Boolean) {
    _selection.getAndUpdate {
      it.copy(
        isTelemetryEnabled = isEnabled
      )
    }
  }

  @Immutable
  data class Selection(
    val scene: String?,
    val renderMode: RenderMode?,
    val stressTestGOSize: Int,
    val isTelemetryEnabled: Boolean,
  )
}
