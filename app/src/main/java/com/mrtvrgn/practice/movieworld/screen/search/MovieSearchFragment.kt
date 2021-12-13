package com.mrtvrgn.practice.movieworld.screen.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mrtvrgn.practice.movieworld.IntentLauncher
import com.mrtvrgn.practice.movieworld.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieSearchFragment : Fragment() {

    companion object {
        private const val KEY_QUERY = "query"

        fun newInstance(query: String?) = MovieSearchFragment().apply {
            arguments = Bundle().apply { putString(KEY_QUERY, query) }
        }
    }

    private val viewModel: MovieSearchViewModel by viewModels()

    private val layoutManager by lazy {
        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var intentLauncher: IntentLauncher
    private val adapter = MovieSearchResultAdapter { movieId ->
        intentLauncher.toWeb(requireContext().getString(R.string.web_intent_url, movieId))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        intentLauncher = context as IntentLauncher
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.search_result_fragment, container, false)

        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_results)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                viewModel.listScrolled(
                    isDown = dy > 0,
                    visibleItemCount = layoutManager.childCount,
                    totalItemCount = layoutManager.itemCount,
                    firstVisibleItemPos = layoutManager.findFirstVisibleItemPosition()
                )
            }
        })

        viewModel.results.observe(viewLifecycleOwner, {
            adapter.loadItems(it)
        })

        viewModel.search(arguments?.getString(KEY_QUERY))
    }
}