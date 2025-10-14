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
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("users").document(user.uid).collection("favorites")
            .get()
            .addOnSuccessListener { result ->
                favoriteMovies.clear()
                for (document in result) {
                    val movie = Movie(
                        title = document.getString("title") ?: "",
                        year = document.getString("year") ?: "",
                        imdbID = document.getString("imdbID") ?: document.id,
                        type = "", // not used here
                        poster = document.getString("poster") ?: ""
                    ).apply {
                        description = document.getString("description") ?: ""
                    }
                    favoriteMovies.add(movie)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load favorites: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
