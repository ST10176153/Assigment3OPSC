package com.example.assignment3opsc.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.assignment3opsc.data.group.GroupDao
import com.example.assignment3opsc.data.group.GroupEntity

@Database(entities = [GroupEntity::class], version = 2, exportSchema = false) // bump to 2
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cinematic.db"
                )
                    .fallbackToDestructiveMigration()   // dev-only: auto-recreate on schema change
                    .build().also { INSTANCE = it }
    }
}}

