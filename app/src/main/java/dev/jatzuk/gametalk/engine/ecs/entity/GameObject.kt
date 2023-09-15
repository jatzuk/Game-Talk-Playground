package dev.jatzuk.gametalk.engine.ecs.entity

import dev.jatzuk.gametalk.engine.ecs.component.Component
import dev.jatzuk.gametalk.engine.ecs.component.transform.Transform
import java.util.UUID

abstract class GameObject(val transform: Transform) : Entity {

  override val id = UUID.randomUUID().leastSignificantBits // any unique id
  var tag: String? = null

  val components = mutableSetOf<Component>()

  open fun onUpdate(deltaTime: Double) = Unit

  override fun addComponent(component: Component) {
    components.add(component)
  }

  override fun deleteComponent(component: Component) {
    components.remove(component)
  }

  inline fun <reified T: Component> getComponent(): T? = components.find { it is T } as T?
}