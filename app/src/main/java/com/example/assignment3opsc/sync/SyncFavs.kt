package com.example.assignment3opsc.sync

import android.content.Context
import androidx.work.*
import com.example.assignment3opsc.work.SyncFavoritesWorker

object SyncFavs {
    fun enqueue(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val req = OneTimeWorkRequestBuilder<SyncFavoritesWorker>()
            .setConstraints(constraints)
            .addTag("syncFavs")
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork("syncFavs", ExistingWorkPolicy.KEEP, req)
    }
}
