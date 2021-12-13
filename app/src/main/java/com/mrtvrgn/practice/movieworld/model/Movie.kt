package com.mrtvrgn.practice.movieworld.model

import com.squareup.moshi.Json

data class Movie(
    val id: Int,
    val title: String,
    @Json(name = "overview") val description: String,
    @Json(name = "vote_average") val rating: Double,
    @Json(name = "poster_path") val posterExtension: String?, // /f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg
    @Json(name = "release_date") val releaseDate: String // 1999-03-30
)

data class MovieSearchResponse(
    val page: Int,
    @Json(name = "total_pages") val totalPages: Int,
    val results: List<Movie>
)