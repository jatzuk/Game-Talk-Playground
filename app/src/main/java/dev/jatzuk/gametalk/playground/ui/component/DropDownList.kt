package dev.jatzuk.gametalk.playground.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownList(
  items: List<String>,
  selectedItemIndex: Int,
  onItemSelected: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  var expanded by remember { mutableStateOf(false) }
  var selectedIndex by remember { mutableIntStateOf(selectedItemIndex) }

  val value = if (items.isEmpty()) "" else items[selectedIndex]

  ExposedDropdownMenuBox(
    modifier = modifier,
    expanded = expanded,
    onExpandedChange = { expanded = !expanded }
  ) {
    TextField(
      value = value,
      onValueChange = {},
      readOnly = true,
      singleLine = true,
      trailingIcon = {
        ExposedDropdownMenuDefaults.TrailingIcon(
          expanded = expanded
        )
      },
      modifier = Modifier
        .menuAnchor()
        .fillMaxWidth()
    )

    DropdownMenu(
      modifier = Modifier.fillMaxWidth(),
      expanded = expanded,
      onDismissRequest = { expanded = false }
    ) {
      items.forEachIndexed { index, item ->
        DropdownMenuItem(
          text = { Text(text = item) },
          onClick = {
            selectedIndex = index
            expanded = false
            onItemSelected(selectedIndex)
          }
        )
      }
    }
  }
}