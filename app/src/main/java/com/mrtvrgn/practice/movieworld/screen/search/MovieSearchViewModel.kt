package com.mrtvrgn.practice.movieworld.screen.search

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrtvrgn.practice.movieworld.repository.MovieSearchRepository
import com.mrtvrgn.practice.movieworld.util.toMonthDayYear
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MovieSearchViewModel @Inject constructor(private val repo: MovieSearchRepository) : ViewModel() {

    val results: MutableLiveData<List<MediaSearchItemModel>> = MediatorLiveData()
    private var page = 1
    private var canLoadMore = true
    private var isLoading = false

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var currentQuery: String? = null

    fun search(query: String?, nextPage: Boolean = false) {

        if (nextPage) {
            page++
        } else if (currentQuery != currentQuery) {
            page = 1
        }

        currentQuery = query
        viewModelScope.launch {

            try {
                val response = repo.searchMovies(query.orEmpty(), page)

                val items = response.results.map {
                    MediaSearchItemModel(
                        id = it.id,
                        imageUrl = POSTER_MAIN_URL + it.posterExtension,
                        title = it.title,
                        date = it.releaseDate.toMonthDayYear(),
                        description = it.description,
                        rate = it.rating
                    )
                }

                if (nextPage) {
                    results.postValue(results.value?.plus(items))
                } else {
                    results.postValue(items)
                }


                isLoading = false
                canLoadMore = page < response.totalPages
            } catch (exp: Exception) {
                Log.e("MovieSearchVM", "Fetching", exp)
                isLoading = false
            }
        }
    }

    fun listScrolled(
        isDown: Boolean,
        visibleItemCount: Int,
        totalItemCount: Int,
        firstVisibleItemPos: Int
    ) {
        if (isDown && !isLoading && canLoadMore) {
            if ((visibleItemCount + firstVisibleItemPos) >= totalItemCount) {
                isLoading = true
                search(currentQuery, nextPage = true)
            }
        }
    }


    companion object {
        private const val POSTER_MAIN_URL = "https://www.themoviedb.org/t/p/w440_and_h660_face"
    }
}