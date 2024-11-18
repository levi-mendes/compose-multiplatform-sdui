package br.com.developes.sdui.layout.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.developes.sdui.SDCLibrary
import br.com.developes.sdui.SDCLibrary.Companion.launchHandling
import br.com.developes.sdui.ServerDrivenNode
import br.com.developes.sdui.layout.Layout
import br.com.developes.sdui.layout.dp
import br.com.developes.sdui.utils.toColor

class SDCButtonText(val node: ServerDrivenNode, val state: MutableMap<String, String>) : Layout {
    private var modifier = Modifier.fromNode(node)
    private val enabled = node.propertyState("enabled", state)?.toBoolean()
    private val text = node.propertyState("text", state)!!
    private val actions = node.propertyNodes("onClick")
    private val roundedCornerShape = node.property("roundedCornerShape")?.dp ?: 0.dp
    private val backgroundColor = node.property("backgroundColor")
    private val color = node.property("color")

    @Composable
    override fun Content() {

        var isEnabled by remember { mutableStateOf(enabled ?: true) }
        val action = SDCLibrary.loadActions(actions)
        // Creates a CoroutineScope bound to the Content's lifecycle
        val scope = rememberCoroutineScope()

        Button(
            modifier = modifier,
            enabled = isEnabled,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = backgroundColor?.toColor() ?: MaterialTheme.colors.primary
            ),
            onClick = {
                isEnabled = false
                scope.launchHandling(after = { isEnabled = true }) {
                    action.invoke(node, state)
                }
            },
            shape = RoundedCornerShape(roundedCornerShape)
        ) {
            Text(
                text = text,
                color = color?.toColor() ?: MaterialTheme.colors.onPrimary
            )
        }
    }
}