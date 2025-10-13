package com.example.assignment3opsc

class Movie(
    var title: String,
    var year: String,
    var imdbID: String,
    var type: String,
    var poster: String,
    var studio: String = "",            // new
    var criticsRating: String = ""     // new
) {
    var description: String = "" // Required for FavoriteMovieAdapter
}
