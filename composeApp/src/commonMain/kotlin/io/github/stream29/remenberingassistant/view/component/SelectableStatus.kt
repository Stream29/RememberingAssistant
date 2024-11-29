package io.github.stream29.remenberingassistant.view.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SelectableStatus(
    expandedState: MutableState<Boolean>,
    selected: @Composable RowScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit
) = Column(horizontalAlignment = Alignment.End) {
    var expanded by expandedState
    Button(onClick = { expanded = true }) { selected() }
    if (expanded) {
        Box(modifier = Modifier.align(Alignment.End)) {
            DropdownMenu(
                onDismissRequest = { expanded = false },
                expanded = expanded,
            ) {
                content()
            }
        }
    }
}