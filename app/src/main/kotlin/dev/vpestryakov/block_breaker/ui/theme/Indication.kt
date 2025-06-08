package dev.vpestryakov.block_breaker.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data object ScaleIndicationNodeFactory : IndicationNodeFactory {

  override fun create(interactionSource: InteractionSource): DelegatableNode = ScaleIndicationNode(interactionSource)
}

private class ScaleIndicationNode(private val interactionSource: InteractionSource) :
  Modifier.Node(), DrawModifierNode {

  private val animatedScalePercent = Animatable(1f)

  override fun onAttach() {
    coroutineScope.launch {
      interactionSource.interactions.collectLatest { interaction ->
        when (interaction) {
          is PressInteraction.Press -> animateToPressed()
          is PressInteraction.Release -> animateToResting()
          is PressInteraction.Cancel -> animateToResting()
        }
      }
    }
  }

  override fun ContentDrawScope.draw() {
    scale(scale = animatedScalePercent.value, block = { this@draw.drawContent() })
  }

  private suspend fun animateToPressed() {
    animatedScalePercent.animateTo(targetValue = 0.9F, animationSpec = spring())
  }

  private suspend fun animateToResting() {
    animatedScalePercent.animateTo(targetValue = 1F, animationSpec = spring())
  }
}