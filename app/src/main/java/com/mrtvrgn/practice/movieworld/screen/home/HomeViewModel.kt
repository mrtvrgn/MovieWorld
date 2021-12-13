package com.mrtvrgn.practice.movieworld.screen.home

import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrtvrgn.practice.movieworld.screen.search.MovieSearchFragment

class HomeViewModel : ViewModel() {

    val displayingFragment: MutableLiveData<Fragment> = MediatorLiveData()

    fun search(query: String?) {
        displayingFragment.postValue(MovieSearchFragment.newInstance(query))
    }
}