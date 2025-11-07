package com.example.assignment3opsc.data.favorite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Local cache of a user's favorite movie.
 * - primary key is the OMDb imdbID
 * - `pending` is used for offline sync (1 = needs upload, 0 = synced)
 */
@Entity(tableName = "favorites")
data class FavoriteEntity(

    @PrimaryKey
    @ColumnInfo(name = "imdbID")
    val imdbId: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "year")
    val year: String,

    @ColumnInfo(name = "poster")
    val poster: String,

    @ColumnInfo(name = "description")
    val description: String = "",

    // optional extras – handy for your UI, safe to keep nullable
    @ColumnInfo(name = "studio")
    val studio: String? = null,

    @ColumnInfo(name = "criticsRating")
    val criticsRating: String? = null,

    // used by DAO queries like: WHERE pending = 1
    @ColumnInfo(name = "pending")
    val pending: Int = 0
)
