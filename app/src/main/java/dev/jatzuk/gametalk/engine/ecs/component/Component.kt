package dev.jatzuk.gametalk.engine.ecs.component

import dev.jatzuk.gametalk.engine.ecs.entity.GameObject

interface Component {

  val gameObject: GameObject
}