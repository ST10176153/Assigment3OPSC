package com.example.assignment3opsc.data.favorite

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY title")
    fun getAll(): LiveData<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE imdbID = :imdbId")
    suspend fun deleteById(imdbId: String)

    @Query("UPDATE favorites SET description = :desc WHERE imdbID = :imdbId")
    suspend fun updateDescription(imdbId: String, desc: String)

    @Query("SELECT * FROM favorites WHERE pending = 1")
    suspend fun pending(): List<FavoriteEntity>   // <-- must return List

    @Query("UPDATE favorites SET pending = 0 WHERE imdbID = :imdbId")
    suspend fun markSynced(imdbId: String)



}
