package com.example.assignment3opsc

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class FavoriteMovieAdapter(
    private val context: Context,
    private val favorites: MutableList<Movie>
) : RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMovieViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.favorite_movie_item, parent, false)
        return FavoriteMovieViewHolder(view)
    }

    override fun getItemCount(): Int = favorites.size

    override fun onBindViewHolder(holder: FavoriteMovieViewHolder, position: Int) {
        val movie = favorites[position]

        // Auto-fill missing studio and criticsRating in Firestore
        val updates = mutableMapOf<String, Any>()
        if (movie.studio.isBlank()) {
            movie.studio = "Warner Bros" // or randomStudio() if you want
            updates["studio"] = movie.studio
        }
        if (movie.criticsRating.isBlank()) {
            movie.criticsRating = "${(70..95).random()}%"
            updates["criticsRating"] = movie.criticsRating
        }

        if (updates.isNotEmpty()) {
            FirebaseFirestore.getInstance().collection("favorites")
                .document(movie.imdbID)
                .update(updates)
                .addOnSuccessListener {
                    Toast.makeText(context, "Movie info updated", Toast.LENGTH_SHORT).show()
                }
        }

        // Load data
        holder.titleText.text = movie.title
        holder.studioText.text = "Studio: ${movie.studio}"
        holder.criticsRatingText.text = "Critics Rating: ${movie.criticsRating}"
        holder.editPlot.setText(movie.description)
        Picasso.get().load(movie.poster).into(holder.posterImage)

        // Rest of your buttons: Update, Delete, Remove...
        holder.buttonUpdatePlot.setOnClickListener {
            val newDesc = holder.editPlot.text.toString().trim()
            if (newDesc.isNotEmpty()) {
                FirebaseFirestore.getInstance().collection("favorites")
                    .document(movie.imdbID)
                    .update("description", newDesc)
                    .addOnSuccessListener {
                        movie.description = newDesc
                        notifyItemChanged(position)
                        Toast.makeText(context, "Description updated!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(context, "Description cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        holder.buttonDeletePlot.setOnClickListener {
            FirebaseFirestore.getInstance().collection("favorites")
                .document(movie.imdbID)
                .update("description", "")
                .addOnSuccessListener {
                    movie.description = ""
                    holder.editPlot.setText("")
                    notifyItemChanged(position)
                    Toast.makeText(context, "Description cleared", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Clear failed", Toast.LENGTH_SHORT).show()
                }
        }

        holder.buttonRemoveFavorite.setOnClickListener {
            FirebaseFirestore.getInstance().collection("favorites")
                .document(movie.imdbID)
                .delete()
                .addOnSuccessListener {
                    favorites.removeAt(position)
                    notifyItemRemoved(position)
                    Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Remove failed", Toast.LENGTH_SHORT).show()
                }
        }
    }


    class FavoriteMovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val posterImage: ImageView = view.findViewById(R.id.posterImage)
        val titleText: TextView = view.findViewById(R.id.titleText)
        val studioText: TextView = view.findViewById(R.id.studioText)
        val criticsRatingText: TextView = view.findViewById(R.id.criticsRatingText)
        val editPlot: EditText = view.findViewById(R.id.editPlot)
        val buttonUpdatePlot: Button = view.findViewById(R.id.buttonUpdatePlot)
        val buttonDeletePlot: Button = view.findViewById(R.id.buttonDeletePlot)
        val buttonRemoveFavorite: Button = view.findViewById(R.id.buttonRemoveFavorite)
    }
}
