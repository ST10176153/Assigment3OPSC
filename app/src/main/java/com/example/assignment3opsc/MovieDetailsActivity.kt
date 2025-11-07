package com.example.assignment3opsc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.assignment3opsc.data.AppDatabase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import com.example.assignment3opsc.data.favorite.FavoriteEntity
import com.example.assignment3opsc.work.SyncFavoritesWorker

class MovieDetailsActivity : AppCompatActivity() {

    // Views
    private lateinit var posterImage: ImageView
    private lateinit var titleText: TextView
    private lateinit var yearText: TextView
    private lateinit var ratingText: TextView
    private lateinit var studioText: TextView
    private lateinit var plotText: TextView
    private lateinit var buttonAddToFavorites: Button
    private lateinit var buttonViewFavorites: Button

    // Data we keep after fetching
    private var imdbId: String = ""
    private var movieTitle: String = ""
    private var year: String = ""
    private var poster: String = ""
    private var plot: String = ""
    private var rating: String = ""
    private var studio: String = ""

    // OMDb
    private val client = OkHttpClient()
    private val apiKey = "147bd025" // your key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        // Toolbar back
        findViewById<Toolbar>(R.id.toolbar)?.let { tb ->
            setSupportActionBar(tb)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.title = ""
        }

        // Bind views
        posterImage = findViewById(R.id.posterImageView)
        titleText = findViewById(R.id.titleTextView)
        yearText = findViewById(R.id.yearTextView)
        ratingText = findViewById(R.id.ratingTextView)
        studioText = findViewById(R.id.studioTextView)
        plotText = findViewById(R.id.plotTextView)
        buttonAddToFavorites = findViewById(R.id.buttonAddToFavorites)
        buttonViewFavorites = findViewById(R.id.buttonViewFavorites)

        // imdbID is passed from the list item
        imdbId = intent.getStringExtra("imdbID") ?: ""

        if (imdbId.isBlank()) {
            Toast.makeText(this, "Missing movie id", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Load details
        fetchMovieDetails(imdbId)

        // Add to favorites
        buttonAddToFavorites.setOnClickListener {
            addToFavorites()
        }

        // View favorites screen
        buttonViewFavorites.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun fetchMovieDetails(id: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val url = "https://www.omdbapi.com/?apikey=$apiKey&i=$id&plot=full"
                val req = Request.Builder().url(url).build()
                val res = client.newCall(req).execute()
                val body = res.body?.string() ?: ""

                val obj = JSONObject(body)
                if (obj.optString("Response") != "True") {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MovieDetailsActivity, "Movie not found", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    return@launch
                }

                // Collect fields
                movieTitle = obj.optString("Title")
                year = obj.optString("Year")
                rating = obj.optString("Rated")
                studio = obj.optString("Production", "Unknown")
                plot = obj.optString("Plot")
                poster = obj.optString("Poster")

                // Update UI
                withContext(Dispatchers.Main) {
                    titleText.text = movieTitle
                    yearText.text = "Year: $year"
                    ratingText.text = "Rated: $rating"
                    studioText.text = "Studio: $studio"
                    plotText.text = plot
                    if (poster.isNotBlank() && poster != "N/A") {
                        Picasso.get().load(poster).into(posterImage)
                    } else {
                        posterImage.setImageResource(R.drawable.no_image_available) // optional placeholder
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MovieDetailsActivity, "Failed to load details", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun addToFavorites() {
        if (imdbId.isBlank() || movieTitle.isBlank()) {
            Toast.makeText(this, "Details not ready yet", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val db = AppDatabase.get(this@MovieDetailsActivity)
                db.favoriteDao().upsert(
                    FavoriteEntity(
                        imdbId = imdbId,
                        title = movieTitle,
                        year = year,
                        poster = poster,
                        description = plot,
                        pending = 1,   // mark for sync
                        synced = 0
                    )
                )

                // queue background sync (make sure your worker has this companion helper)
                SyncFavoritesWorker.enqueue(this@MovieDetailsActivity)

                Toast.makeText(
                    this@MovieDetailsActivity,
                    "Added to favorites",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Toast.makeText(
                    this@MovieDetailsActivity,
                    "Could not add to favorites",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
