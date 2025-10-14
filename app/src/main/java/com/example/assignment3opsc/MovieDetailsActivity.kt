package com.example.assignment3opsc

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MovieDetailsActivity : AppCompatActivity() {
    private val API_KEY = "147bd025"

    private var titleTextView: TextView? = null
    private var yearTextView: TextView? = null
    private var ratingTextView: TextView? = null
    private var studioTextView: TextView? = null
    private var plotTextView: TextView? = null
    private var posterImageView: ImageView? = null
    private var addToFavoritesButton: Button? = null

    private var currentMovieJson: JSONObject? = null
    private var currentImdbID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        findViewById<Button>(R.id.buttonViewFavorites).setOnClickListener {
            startActivity(Intent(this, FavoriteMoviesActivity::class.java))
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = ""
        }

        titleTextView = findViewById(R.id.titleTextView)
        yearTextView = findViewById(R.id.yearTextView)
        ratingTextView = findViewById(R.id.ratingTextView)
        studioTextView = findViewById(R.id.studioTextView)
        plotTextView = findViewById(R.id.plotTextView)
        posterImageView = findViewById(R.id.posterImageView)
        addToFavoritesButton = findViewById(R.id.buttonAddToFavorites)

        val imdbID = intent.getStringExtra("imdbID")
        if (imdbID.isNullOrEmpty()) {
            Toast.makeText(this, "Error: Missing movie ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        currentImdbID = imdbID
        fetchMovieDetails(imdbID)

        addToFavoritesButton?.setOnClickListener {
            currentMovieJson?.let {
                saveToFavorites(it)
            } ?: Toast.makeText(this, "Movie not loaded yet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchMovieDetails(imdbID: String) {
        val client = OkHttpClient()
        val url = "https://www.omdbapi.com/?apikey=$API_KEY&i=$imdbID&plot=short"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val res = response.body?.string() ?: return
                try {
                    val obj = JSONObject(res)
                    currentMovieJson = obj

                    val title = obj.optString("Title", "N/A")
                    val year = obj.optString("Year", "N/A")
                    val rating = obj.optString("Rated", "N/A")
                    val studio = obj.optString("Production", "Unknown")
                    val plot = obj.optString("Plot", "No description available")
                    val poster = obj.optString("Poster", "")

                    runOnUiThread {
                        titleTextView?.text = title
                        yearTextView?.text = "Year: $year"
                        ratingTextView?.text = "Rated: $rating"
                        studioTextView?.text = "Studio: $studio"
                        plotTextView?.text = plot

                        if (poster != "N/A" && poster.isNotEmpty()) {
                            Picasso.get().load(poster).into(posterImageView)
                        } else {
                            posterImageView?.setImageResource(R.drawable.no_image_available)
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun saveToFavorites(movieJson: JSONObject) {
        val user = FirebaseAuth.getInstance().currentUser
            ?: return Toast.makeText(this, "You must be logged in", Toast.LENGTH_SHORT).show()

        val imdbID = movieJson.optString("imdbID")
        if (imdbID.isEmpty()) {
            Toast.makeText(this, "Invalid movie data", Toast.LENGTH_SHORT).show()
            return
        }

        val favoriteMovie = hashMapOf(
            "title" to movieJson.optString("Title"),
            "year" to movieJson.optString("Year"),
            "imdbID" to imdbID,
            "poster" to movieJson.optString("Poster"),
            "description" to movieJson.optString("Plot")
        )

        FirebaseFirestore.getInstance()
            .collection("users").document(user.uid)
            .collection("favorites").document(imdbID)
            .set(favoriteMovie)
            .addOnSuccessListener {
                Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add to Favorites: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
