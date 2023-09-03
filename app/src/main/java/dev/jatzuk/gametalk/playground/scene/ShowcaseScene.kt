package dev.jatzuk.gametalk.playground.scene

import android.content.Context
import dev.jatzuk.gametalk.engine.SceneConfigRepository
import dev.jatzuk.gametalk.engine.ecs.component.rect.RectComponent
import dev.jatzuk.gametalk.engine.ecs.component.transform.Position
import dev.jatzuk.gametalk.engine.ecs.component.transform.Rotation
import dev.jatzuk.gametalk.engine.ecs.component.transform.Scale
import dev.jatzuk.gametalk.engine.ecs.component.transform.Size
import dev.jatzuk.gametalk.engine.ecs.component.transform.Transform
import dev.jatzuk.gametalk.engine.scene.AbstractScene
import dev.jatzuk.gametalk.engine.scripting.RectGameObject
import dev.jatzuk.gametalk.engine.telemetry.TelemetryRepository
import kotlin.random.Random

class ShowcaseScene(
  sceneConfigRepository: SceneConfigRepository,
  telemetryRepository: TelemetryRepository
) : AbstractScene(sceneConfigRepository, telemetryRepository) {

  override fun load(width: Int, height: Int, context: Context) {
    super.load(width, height, context)

    repeat(1) { i ->
      val size = 300f
      val x = Random.nextInt(-width / 2, width / 2).toFloat()
      val y = Random.nextInt(-height / 2, height / 2).toFloat()

      val transform = Transform(
        Position(x, y),
        Rotation(0f),
        Size(size),
        Scale(1f)
      )

      val go = RectGameObject(
        transform = transform,
        text = "$i",
        screenWidth = width,
        screenHeight = height
      ).apply {
        addComponent(RectComponent(this, context))
      }

      addGameObject(go)
    }
  }
}