package com.example.assignment3opsc.data.group

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_groups")
data class GroupEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val emoji: String = "🎬"
)


