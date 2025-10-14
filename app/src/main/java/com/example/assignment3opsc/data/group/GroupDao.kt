package com.example.assignment3opsc.data.group

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GroupDao {
    @Query("SELECT * FROM user_groups ORDER BY id DESC")
    fun getGroups(): LiveData<List<GroupEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(group: GroupEntity)

    @Query("DELETE FROM user_groups WHERE id = :id")
    suspend fun deleteById(id: Long)
}

