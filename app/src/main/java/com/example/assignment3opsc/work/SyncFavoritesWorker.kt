package com.example.assignment3opsc.work   // <- use your real package

import android.content.Context
import androidx.work.*
import com.example.assignment3opsc.data.AppDatabase
import com.example.assignment3opsc.data.favorite.FavoriteEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class SyncFavoritesWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val db = AppDatabase.get(applicationContext)
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return Result.retry()

        val pending = db.favoriteDao().pending()
        if (pending.isEmpty()) return Result.success()

        val col = FirebaseFirestore.getInstance()
            .collection("users").document(uid)
            .collection("favorites")

        try {
            for (f in pending) {
                val data = mapOf(
                    "imdbID" to f.imdbId,
                    "title" to f.title,
                    "year" to f.year,
                    "poster" to f.poster,
                    "description" to f.description
                )
                col.document(f.imdbId).set(data).await()
                db.favoriteDao().markSynced(f.imdbId)
            }
            return Result.success()
        } catch (e: Exception) {
            return Result.retry()
        }
    }

    companion object {
        private const val WORK_NAME = "syncFavs"

        fun enqueue(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = OneTimeWorkRequestBuilder<SyncFavoritesWorker>()
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork(WORK_NAME, ExistingWorkPolicy.KEEP, request)
        }
    }
}
