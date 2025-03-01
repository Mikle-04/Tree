package com.example.three.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.security.MessageDigest

@Entity(tableName = "nodes")
data class Node(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val parentId: Long? = null,
    @ColumnInfo(name = "name") val name: String

) {
    @Ignore
    var children: List<Node> = emptyList() // Игнорируется Room, заполняется вручную
}

// Генерация названия на основе хэша
fun generateNodeName(id: Long, parentId: Long?): String {
    val data = "$id$parentId".toByteArray() // Хэшируем ID и parentId
    val hash = MessageDigest.getInstance("SHA-256").digest(data)
    val last20Bytes = hash.takeLast(20).joinToString("") { byte -> "%02x".format(byte) }
    return "0x$last20Bytes" // Формат как в Ethereum
}