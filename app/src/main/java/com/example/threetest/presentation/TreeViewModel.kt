package com.example.three.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.three.data.Node
import com.example.three.data.NodeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TreeViewModel @Inject constructor(
    private val repository: NodeRepository
) : ViewModel() {
    private val _currentNode = MutableStateFlow<Node?>(null)
    val currentNode: StateFlow<Node?> = _currentNode.asStateFlow()

    private val _children = MutableStateFlow<List<Node>>(emptyList())
    val children: StateFlow<List<Node>> = _children.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initializeRootIfEmpty()
            val root = repository.getRootNode()
            root?.let {
                _currentNode.value = it
                _children.value = repository.getChildren(it.id)
            }
        }
    }

    fun navigateToChild(node: Node) {
        viewModelScope.launch {
            _currentNode.value = node
            _children.value = repository.getChildren(node.id)
        }
    }

    fun navigateToParent() {
        viewModelScope.launch {
            val parentId = _currentNode.value?.parentId
            if (parentId != null) {
                val parent = repository.getChildren(parentId).find { it.id == parentId }
                    ?: repository.getRootNode()
                _currentNode.value = parent
                _children.value = repository.getChildren(parent?.id ?: 0)
            } else {
                val root = repository.getRootNode()
                _currentNode.value = root
                _children.value = repository.getChildren(root?.id ?: 0)
            }
        }
    }

    fun addChild() {
        viewModelScope.launch {
            val parentId = _currentNode.value?.id
            val newNode = repository.addChild(parentId)
            _children.value = _children.value + newNode
        }
    }

    fun deleteNode(node: Node) {
        viewModelScope.launch {
            repository.deleteNode(node)
            _children.value = _children.value.filter { it.id != node.id }
        }
    }
}