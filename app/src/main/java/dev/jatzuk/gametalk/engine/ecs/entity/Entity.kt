package dev.jatzuk.gametalk.engine.ecs.entity

import dev.jatzuk.gametalk.engine.ecs.component.Component

interface Entity {

  val id: Long

  fun addComponent(component: Component)
  fun deleteComponent(component: Component)
}