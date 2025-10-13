package com.example.assignment3opsc


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FavoriteMoviesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavoriteMovieAdapter
    private lateinit var favoriteMovies: MutableList<Movie>
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_movies)


        // Setup Toolbar with back arrow
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""

        // RecyclerView setup
        recyclerView = findViewById(R.id.recyclerViewFavorites)
        recyclerView.layoutManager = LinearLayoutManager(this)

        favoriteMovies = mutableListOf()
        adapter = FavoriteMovieAdapter(this, favoriteMovies)
        recyclerView.adapter = adapter

        loadFavoriteMovies()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadFavoriteMovies() {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        if (userEmail.isNullOrEmpty()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("favorites")
            .whereEqualTo("userID", userEmail)
            .get()
            .addOnSuccessListener { result ->
                favoriteMovies.clear()
                for (document in result) {
                    val movie = Movie(
                        title = document.getString("title") ?: "",
                        year = document.getString("year") ?: "",
                        imdbID = document.getString("imdbID") ?: document.id,
                        type = document.getString("type") ?: "",
                        poster = document.getString("poster") ?: "",
                        studio = document.getString("studio") ?: "Unknown Studio", // new
                        criticsRating = document.getString("criticsRating") ?: "N/A" // new
                    ).apply {
                        description = document.getString("description") ?: ""
                    }

                    println("Loaded favorite movie: $movie")
                    favoriteMovies.add(movie)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load favorites", Toast.LENGTH_SHORT).show()
                println("Firestore fetch error: ${it.message}")
            }
    }
}
