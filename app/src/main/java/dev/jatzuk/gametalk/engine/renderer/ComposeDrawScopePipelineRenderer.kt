package dev.jatzuk.gametalk.engine.renderer

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.drawscope.DrawScope
import dev.jatzuk.gametalk.engine.ecs.component.rect.RectComponent
import dev.jatzuk.gametalk.engine.ecs.component.sprite.SpriteComponent
import dev.jatzuk.gametalk.engine.ecs.entity.GameObject
import dev.jatzuk.gametalk.engine.ecs.system.sprite.ComposeDrawScopeSpriteRenderer

class ComposeDrawScopePipelineRenderer {

  private val spriteRenderer = ComposeDrawScopeSpriteRenderer()

  fun renderDrawScope(gameObject: GameObject, drawScope: DrawScope) {
    // here are the two ways you can organize component - system relation
    // in this case a System is nested inside a component for the sake of simplicity and less di stuff
    val rectComponent = gameObject.getComponent<RectComponent>()
    rectComponent?.render(drawScope)

    // here are the two ways you can organize component - system relation
    // in this case a System is placed inside a PipelineRenderer
    val spriteComponent = gameObject.getComponent<SpriteComponent>()
    if (spriteComponent != null) {
      spriteRenderer.render(spriteComponent, drawScope)
    }
  }

  @Composable
  fun RenderComposable(gameObject: GameObject) {
    val rectComponent = gameObject.getComponent<RectComponent>()
    rectComponent?.Render()

    val spriteComponent = gameObject.getComponent<SpriteComponent>()
    spriteComponent?.Render()
  }
}