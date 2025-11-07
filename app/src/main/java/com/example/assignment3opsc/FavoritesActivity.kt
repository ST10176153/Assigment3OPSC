package com.example.assignment3opsc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritesActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    // If you already built FavoriteMovieAdapter, use it here. For now we’ll show a tiny placeholder.
    private val adapter = SimpleFavoritesAdapter()  // replace with FavoriteMovieAdapter if you have it

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        supportActionBar?.title = "Favorites"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recycler = findViewById(R.id.favRecycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        // TODO: Replace with your real source (Room or Firestore).
        // This just shows how to wire data into the list.
        adapter.submit(listOf("No data yet – wire Room/Firestore here"))
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
