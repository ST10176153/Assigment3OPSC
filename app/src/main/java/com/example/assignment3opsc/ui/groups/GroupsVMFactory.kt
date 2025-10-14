package com.example.assignment3opsc.ui.groups


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GroupsVMFactory(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GroupsViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
