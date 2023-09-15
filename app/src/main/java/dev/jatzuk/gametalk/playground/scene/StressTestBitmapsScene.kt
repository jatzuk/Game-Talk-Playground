package dev.jatzuk.gametalk.playground.scene

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import dev.jatzuk.gametalk.R
import dev.jatzuk.gametalk.engine.SceneConfigRepository
import dev.jatzuk.gametalk.engine.ecs.component.sprite.Sprite
import dev.jatzuk.gametalk.engine.ecs.component.sprite.SpriteComponent
import dev.jatzuk.gametalk.engine.ecs.component.transform.Position
import dev.jatzuk.gametalk.engine.ecs.component.transform.Rotation
import dev.jatzuk.gametalk.engine.ecs.component.transform.Scale
import dev.jatzuk.gametalk.engine.ecs.component.transform.Size
import dev.jatzuk.gametalk.engine.ecs.component.transform.Transform
import dev.jatzuk.gametalk.engine.scene.AbstractScene
import dev.jatzuk.gametalk.engine.scripting.CarGameObject
import dev.jatzuk.gametalk.engine.telemetry.TelemetryRepository
import kotlin.random.Random

class StressTestBitmapsScene(
  sceneConfigRepository: SceneConfigRepository,
  telemetryRepository: TelemetryRepository
) : AbstractScene(sceneConfigRepository, telemetryRepository) {

  override fun load(width: Int, height: Int, context: Context) {
    super.load(width, height, context)

    repeat(sceneConfigRepository.stressTestCountSize) {
      val drawable = AppCompatResources.getDrawable(context, R.drawable.car)
      requireNotNull(drawable) { "Requested drawable for sprite not found" }
      val bitmap = drawable.toBitmap()
      val sprite = Sprite(bitmap)

      val x = Random.nextInt(-width / 2 + (sprite.width / 2), width / 2 - (sprite.width / 2)).toFloat()
      val y = Random.nextInt(-height / 2 + (sprite.height / 2), height / 2 - (sprite.height / 2)).toFloat()

      val transform = Transform(
        Position(x, y),
        Rotation(0f),
        Size(sprite.width.toFloat(), sprite.height.toFloat()),
        Scale(1f)
      )

      val go = CarGameObject(
        transform = transform,
        screenWidth = width,
        screenHeight = height
      ).apply {
        addComponent(SpriteComponent(sprite, this))
      }

      addGameObject(go)
    }
  }
}