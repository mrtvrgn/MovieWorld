package com.mrtvrgn.practice.movieworld.screen.showpiece

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrtvrgn.practice.movieworld.model.Movie
import com.mrtvrgn.practice.movieworld.repository.MovieSearchRepository
import com.mrtvrgn.practice.movieworld.util.toYear
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@HiltViewModel
class ShowPieceViewModel @Inject constructor(private val repo: MovieSearchRepository) : ViewModel() {

    val trendingMedia: MutableLiveData<List<HorizontalMediaItemModel>> = MediatorLiveData()
    val topRatedMedia: MutableLiveData<List<HorizontalMediaItemModel>> = MediatorLiveData()
    val upcomingMedia: MutableLiveData<List<HorizontalMediaItemModel>> = MediatorLiveData()

    fun loadData() {
        viewModelScope.launch {
            try {

                val trendingAsync = async {
                    repo.getWeeklyTrendingMovies().results.map { it.toHorizontalMediaModel() }
                }

                val topRatedAsync = async {
                    repo.getTopRatedMovies().results.map { it.toHorizontalMediaModel() }
                }

                val upcomingAsync = async {
                    repo.getUpcomingMovies().results.map { it.toHorizontalMediaModel() }
                }

                trendingMedia.postValue(trendingAsync.await())
                topRatedMedia.postValue(topRatedAsync.await())
                upcomingMedia.postValue(upcomingAsync.await())

            } catch (exp: Exception) {
                Log.e("ERROR", "FETCHING", exp)
            }
        }
    }

    private fun Movie.toHorizontalMediaModel(): HorizontalMediaItemModel {
        return HorizontalMediaItemModel(
            id = this.id,
            imageUrl = POSTER_MAIN_URL + this.posterExtension,
            title = this.title,
            year = this.releaseDate.toYear()
        )
    }

    companion object {
        private const val POSTER_MAIN_URL = "https://www.themoviedb.org/t/p/w440_and_h660_face" //TODO StringProvider
    }
}