package com.example.assignment3opsc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MovieActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView

    private val movieList: MutableList<Movie> = mutableListOf()
    private lateinit var adapter: MovieAdapter

    private val apiKey = "147bd025"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "" // optional


        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        recyclerView = findViewById(R.id.recyclerView)

        adapter = MovieAdapter(movieList, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchButton.setOnClickListener {
            searchMovies()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
        return true
    }


    private fun searchMovies() {
        val query = searchEditText.text.toString().trim()
        if (query.isEmpty()) return

        val client = OkHttpClient()
        val url = "https://www.omdbapi.com/?apikey=$apiKey&s=${query.replace(" ", "+")}"

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val res = response.body?.string() ?: return

                try {
                    val obj = JSONObject(res)

                    if (obj.optString("Response") == "False") {
                        runOnUiThread {
                            Toast.makeText(
                                this@MovieActivity,
                                "MOVIE NOT FOUND",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        return
                    }

                    val arr = obj.getJSONArray("Search")
                    movieList.clear()

                    for (i in 0 until arr.length()) {
                        val item = arr.getJSONObject(i)
                        movieList.add(
                            Movie(
                                item.getString("Title"),
                                item.getString("Year"),
                                item.getString("imdbID"),
                                item.getString("Type"),
                                item.getString("Poster")
                            )
                        )
                    }

                    runOnUiThread {
                        adapter.notifyDataSetChanged()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
    }
}
