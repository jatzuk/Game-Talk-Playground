package dev.jatzuk.gametalk.playground.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jatzuk.gametalk.engine.util.safeInt

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeScreen(
  scenes: List<String> = listOf("scene1", "scene2"),
  selectedSceneItemIndex: Int = 0,
  onSceneSelected: (Int) -> Unit = {},

  renderTypes: List<String> = listOf("mode1"),
  onRenderTypeSelected: (Int) -> Unit = {},
  selectedRenderTypeIndex: Int = 0,

  selectedGOSize: Int = 1,
  onGOSizeChanged: (Int) -> Unit = {},

  isTelemetryToggleChecked: Boolean = true,
  onTelemetryToggleChecked: (Boolean) -> Unit = {},
  onClick: () -> Unit = {}
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .padding(32.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceAround
  ) {

    DropDownList(
      items = scenes,
      selectedItemIndex = selectedSceneItemIndex,
      onItemSelected = { index -> onSceneSelected(index) },
    )

    DropDownList(
      items = renderTypes,
      selectedItemIndex = selectedRenderTypeIndex,
      onItemSelected = { index -> onRenderTypeSelected(index) }
    )

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.CenterHorizontally),
      horizontalArrangement = Arrangement.End,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(text = "Stress scenes\n GO size")

      Spacer(modifier = Modifier.padding(8.dp))

      var textState by remember {
        mutableStateOf(
          TextFieldValue(selectedGOSize.toString())
        )
      }
      TextField(
        modifier = Modifier
          .wrapContentWidth(align = Alignment.End),
        value = textState.copy(selection = TextRange(textState.text.length)),
        onValueChange = {
          val digitsString = it.text.filter(Char::isDigit)

          textState = it.copy(
            text = digitsString,
            selection = TextRange(digitsString.length)
          )

          onGOSizeChanged(digitsString.safeInt(1))
        },
        singleLine = true,
        visualTransformation = VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
      )
    }

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.CenterHorizontally),
      horizontalArrangement = Arrangement.End,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(text = "Track telemetry")
      Checkbox(
        checked = isTelemetryToggleChecked,
        onCheckedChange = { onTelemetryToggleChecked(it) }
      )
    }

    Button(
      onClick = { onClick() }
    ) {
      Text(text = "start")
    }
  }
}