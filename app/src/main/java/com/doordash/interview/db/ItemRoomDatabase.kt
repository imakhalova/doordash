package com.doordash.interview.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.doordash.interview.list.ListItem

@Database(entities = [ListItem::class], version = 1, exportSchema = false)
abstract class ItemRoomDatabase : RoomDatabase() {
    abstract fun wordDao(): NetworkItemDao

    companion object {
        fun getDatabase(context: Context): ItemRoomDatabase {
            return Room.databaseBuilder(
                    context.applicationContext,
                    ItemRoomDatabase::class.java,
                    "ItemDB"
                ).build()
        }
    }
}
