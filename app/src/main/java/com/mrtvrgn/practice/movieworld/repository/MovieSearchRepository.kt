package com.mrtvrgn.practice.movieworld.repository

import com.mrtvrgn.practice.movieworld.network.service.MovieService
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class MovieSearchRepository @Inject constructor(private val service: MovieService) {

    suspend fun getWeeklyTrendingMovies() = withContext(Dispatchers.IO) {
        service.fetchTrendingMoviesOfWeek()
    }

    suspend fun getTopRatedMovies() = withContext(Dispatchers.IO) {
        service.fetchTopRatedMovies()
    }

    suspend fun getUpcomingMovies() = withContext(Dispatchers.IO) {
        service.fetchUpcomingMovies()
    }

    suspend fun searchMovies(query: String, page: Int) = withContext(Dispatchers.IO) {
        service.search(query, page)
    }
}