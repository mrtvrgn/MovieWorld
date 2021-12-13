package com.mrtvrgn.practice.movieworld.network.service

import com.mrtvrgn.practice.movieworld.model.MovieSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("search/movie")
    suspend fun search(
        @Query(value = "query") query: String,
        @Query(value = "page") page: Int = 1
    ): MovieSearchResponse

    @GET("trending/movie/week")
    suspend fun fetchTrendingMoviesOfWeek(@Query(value = "page") page: Int = 1): MovieSearchResponse

    @GET("movie/top_rated")
    suspend fun fetchTopRatedMovies(): MovieSearchResponse

    @GET("movie/upcoming")
    suspend fun fetchUpcomingMovies(): MovieSearchResponse
}