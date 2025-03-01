package com.example.three.data

import javax.inject.Inject

class NodeRepository @Inject constructor(
    private val nodeDao: NodeDao
) {
    suspend fun getRootNode(): Node? {
        val root = nodeDao.getRootNode()
        if (root != null) {
            root.children = getChildren(root.id)
        }
        return root
    }

    suspend fun getChildren(parentId: Long): List<Node> {
        val children = nodeDao.getChildren(parentId)
        children.forEach { it.children = getChildren(it.id) } // Рекурсивно заполняем детей
        return children
    }

    suspend fun addChild(parentId: Long?): Node {
        val newNode = Node(parentId = parentId, name = generateNodeName(System.currentTimeMillis(), parentId))
        val id = nodeDao.insert(newNode)
        return newNode.copy(id = id)
    }

    suspend fun deleteNode(node: Node) {
        deleteNodeWithChildren(node)
    }

    private suspend fun deleteNodeWithChildren(node: Node) {
        node.children.forEach { deleteNodeWithChildren(it) }
        nodeDao.delete(node)
    }

    suspend fun initializeRootIfEmpty() {
        if (nodeDao.getRootNode() == null) {
            nodeDao.insert(Node(name = generateNodeName(0, null)))
        }
    }
}