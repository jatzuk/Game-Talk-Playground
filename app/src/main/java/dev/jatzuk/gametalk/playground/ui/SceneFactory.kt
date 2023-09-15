package dev.jatzuk.gametalk.playground.ui

import dev.jatzuk.gametalk.engine.SceneConfigRepository
import dev.jatzuk.gametalk.engine.scene.AbstractScene
import dev.jatzuk.gametalk.playground.scene.ShowcaseScene
import dev.jatzuk.gametalk.playground.scene.StressTestBitmapsScene
import dev.jatzuk.gametalk.playground.scene.StressTestRectanglesScene
import dev.jatzuk.gametalk.engine.telemetry.TelemetryRepository

class SceneFactory(
  private val sceneConfigRepository: SceneConfigRepository
) {

  fun create(name: String, telemetryRepository: TelemetryRepository): AbstractScene = when (name) {
    ShowcaseScene::class.java.simpleName -> {
      ShowcaseScene(sceneConfigRepository, telemetryRepository)
    }

    StressTestRectanglesScene::class.java.simpleName -> {
      StressTestRectanglesScene(sceneConfigRepository, telemetryRepository)
    }

    StressTestBitmapsScene::class.java.simpleName -> {
      StressTestBitmapsScene(sceneConfigRepository, telemetryRepository)
    }

    else -> error("Unknown scene type requested: $name, make sure your newly created scene is added to the SceneFactory.scenes")
  }

  companion object {
    val scenes = listOf(
      ShowcaseScene::class.java,
      StressTestRectanglesScene::class.java,
      StressTestBitmapsScene::class.java
    )
  }
}