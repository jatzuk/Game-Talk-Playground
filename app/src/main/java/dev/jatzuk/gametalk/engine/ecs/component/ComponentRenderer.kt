package dev.jatzuk.gametalk.engine.ecs.component

interface ComponentRenderer<C: Any, T: Any> {

  fun render(component: C, target: T)
}