package com.example.assignment3opsc.data.favorite

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Local cache of a user's favorite movie.
 * - primary key is the OMDb imdbID
 * - `pending` is used for offline sync (1 = needs upload, 0 = synced)
 */
@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val imdbId: String,
    val title: String,
    val year: String,
    val poster: String,
    val description: String,
    val pending: Int = 1,            // default pending=1 for unsynced rows
    val synced: Int
)
