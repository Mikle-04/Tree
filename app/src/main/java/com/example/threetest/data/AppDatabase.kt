package com.example.three.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Node::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun nodeDao(): NodeDao
}