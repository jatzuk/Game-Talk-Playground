package dev.jatzuk.gametalk.engine.renderer

import android.graphics.Canvas
import dev.jatzuk.gametalk.engine.ecs.component.rect.RectComponent
import dev.jatzuk.gametalk.engine.ecs.component.sprite.SpriteComponent
import dev.jatzuk.gametalk.engine.ecs.entity.GameObject
import dev.jatzuk.gametalk.engine.ecs.system.sprite.CanvasSpriteRenderer

class CanvasPipelineRenderer {

  private val spriteRenderer = CanvasSpriteRenderer()

  fun renderCanvas(gameObject: GameObject, canvas: Canvas) {
    // here are the two ways you can organize component - system relation
    // in this case a System is nested inside a component for the sake of simplicity and less di stuff
    val rectComponent = gameObject.getComponent<RectComponent>()
    rectComponent?.render(canvas)

    // here are the two ways you can organize component - system relation
    // in this case a System is placed inside a PipelineRenderer
    val spriteComponent = gameObject.getComponent<SpriteComponent>()
    if (spriteComponent != null) {
      spriteRenderer.render(spriteComponent, canvas)
    }
  }
}