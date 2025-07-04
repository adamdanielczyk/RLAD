package com.rlad.shared.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSHomeDirectory

actual class DatabaseFactory {
    actual fun createRoomDatabase(): RoomDatabase.Builder<RladDatabase> {
        val dbFilePath = documentDirectory() + "/rlad_database.db"
        return Room.databaseBuilder<RladDatabase>(
            name = dbFilePath,
        )
    }
    
    @OptIn(ExperimentalForeignApi::class)
    private fun documentDirectory(): String {
        val documentDirectory = NSHomeDirectory() + "/Documents"
        return documentDirectory
    }
}