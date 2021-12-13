package com.mrtvrgn.practice.movieworld.screen.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mrtvrgn.practice.movieworld.R

class MovieSearchResultAdapter(private val onItemClickBehavior: (movieId: Int) -> Unit) :
    RecyclerView.Adapter<MovieSearchResultAdapter.SearchResultMediaViewHolder>() {

    private var items: List<MediaSearchItemModel> = emptyList()

    class SearchResultMediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val title: TextView = itemView.findViewById(R.id.title)
        val date: TextView = itemView.findViewById(R.id.date)
        val description: TextView = itemView.findViewById(R.id.description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultMediaViewHolder {
        return SearchResultMediaViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_vertical_media_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchResultMediaViewHolder, position: Int) {
        items[position].let { media ->
            Glide.with(holder.image).load(media.imageUrl).placeholder(R.drawable.placeholder).into(holder.image)

            holder.title.text = media.title
            holder.date.text = media.date
            holder.description.text = media.description
            holder.itemView.setOnClickListener {
                onItemClickBehavior.invoke(media.id)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun loadItems(items: List<MediaSearchItemModel>) {
        this.items = items
        notifyDataSetChanged()
    }
}