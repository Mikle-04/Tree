package com.example.three.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NodeDao {
    @Query("SELECT * FROM nodes WHERE parentId IS NULL LIMIT 1")
    suspend fun getRootNode(): Node?

    @Query("SELECT * FROM nodes WHERE parentId = :parentId")
    suspend fun getChildren(parentId: Long): List<Node>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(node: Node): Long

    @Delete
    suspend fun delete(node: Node)
}