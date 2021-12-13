package com.mrtvrgn.practice.movieworld.screen.search

data class MediaSearchItemModel(
    val id: Int,
    val imageUrl: String,
    val title: String,
    val date: String,
    val description: String,
    val rate: Double
)