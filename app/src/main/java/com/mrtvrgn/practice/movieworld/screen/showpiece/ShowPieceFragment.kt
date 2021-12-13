package com.mrtvrgn.practice.movieworld.screen.showpiece

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.mrtvrgn.practice.movieworld.IntentLauncher
import com.mrtvrgn.practice.movieworld.R
import com.mrtvrgn.practice.movieworld.screen.search.MovieSearchResultAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowPieceFragment : Fragment() {

    private val viewModel: ShowPieceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_show_piece, container, false)
    }

    private lateinit var intentLauncher: IntentLauncher

    override fun onAttach(context: Context) {
        super.onAttach(context)
        intentLauncher = context as IntentLauncher
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.trendingMedia.observe(viewLifecycleOwner, {
            view.findViewById<RecyclerView>(R.id.recycler_trending).adapter = HorizontalMediaAdapter(it) { movieId ->
                intentLauncher.toWeb(requireContext().getString(R.string.web_intent_url, movieId))
            }
        })
        viewModel.topRatedMedia.observe(viewLifecycleOwner, {
            view.findViewById<RecyclerView>(R.id.recycler_top_rated).adapter = HorizontalMediaAdapter(it) { movieId ->
                intentLauncher.toWeb(requireContext().getString(R.string.web_intent_url, movieId))
            }
        })
        viewModel.upcomingMedia.observe(viewLifecycleOwner, {
            view.findViewById<RecyclerView>(R.id.recycler_upcoming).adapter = HorizontalMediaAdapter(it) { movieId ->
                intentLauncher.toWeb(requireContext().getString(R.string.web_intent_url, movieId))
            }
        })

        viewModel.loadData()
    }
}