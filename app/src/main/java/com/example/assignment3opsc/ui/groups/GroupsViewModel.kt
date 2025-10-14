package com.example.assignment3opsc.ui.groups

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.assignment3opsc.data.AppDatabase
import com.example.assignment3opsc.data.group.GroupRepository

class GroupsViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = GroupRepository(AppDatabase.get(app).groupDao())
    val groups = repo.groups()

    fun createGroup(name: String, emoji: String = "🎬") = viewModelScope.launch {
        repo.create(name.trim(), emoji)
    }

    fun exitGroup(id: Long) = viewModelScope.launch {
        repo.delete(id)
    }
}
