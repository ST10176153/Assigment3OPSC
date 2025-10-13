package com.example.assignment3opsc

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class MovieAdapter(private val movieList: List<Movie>, private val context: Context) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.title.text = movie.title
        holder.year.text = "Year: " + movie.year

        // Load poster if available
        if (!movie.poster.equals("N/A") && !movie.poster.isEmpty()) {
            Picasso.get().load(movie.poster).into(holder.posterImageView)
        } else {
            holder.posterImageView.setImageResource(R.drawable.no_image_available)
        }

        holder.itemView.setOnClickListener { v: View? ->
            val intent = Intent(
                context,
                MovieDetailsActivity::class.java
            )
            intent.putExtra("imdbID", movie.imdbID)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById<TextView>(R.id.titleTextView)
        var year: TextView = itemView.findViewById<TextView>(R.id.yearTextView)
        var posterImageView: ImageView =
            itemView.findViewById<ImageView>(R.id.posterImageView) // Added
        // Make sure this ID exists in item_movie.xml
    }
}
