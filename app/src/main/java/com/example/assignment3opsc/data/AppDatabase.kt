package com.example.assignment3opsc.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.assignment3opsc.data.group.GroupDao
import com.example.assignment3opsc.data.group.GroupEntity
import com.example.assignment3opsc.data.favorite.FavoriteDao
import com.example.assignment3opsc.data.favorite.FavoriteEntity

@Database(
    entities = [GroupEntity::class, FavoriteEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cinematic.db"
                )
                    // dev-only: wipe on schema change so version bump works without a migration
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
    }
}
