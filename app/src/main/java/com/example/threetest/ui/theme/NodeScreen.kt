package com.example.threetest.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.three.data.Node
import com.example.three.presentation.TreeViewModel

@Composable
fun NodeScreen(viewModel: TreeViewModel) {
    val currentNode by viewModel.currentNode.collectAsStateWithLifecycle()
    val children by viewModel.children.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        currentNode?.let { node ->
            Text(text = "Current Node: ${node.name}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Button(onClick = { viewModel.addChild() }) {
                    Text("Add Child")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { viewModel.navigateToParent() },
                    enabled = node.parentId != null
                ) {
                    Text("Go to Parent")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(
                    items = children,
                    key = { child -> child.id }
                ) { child ->
                    NodeItem(
                        child = child,
                        onClick = { viewModel.navigateToChild(child) },
                        onDelete = { viewModel.deleteNode(child) }
                    )
                }
            }
        } ?: Text("Loading...")
    }
}
@Composable
fun NodeItem(
    child: Node,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = child.name, modifier = Modifier.weight(1f))
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Node",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}