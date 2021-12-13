package com.mrtvrgn.practice.movieworld.screen.showpiece

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mrtvrgn.practice.movieworld.R

class HorizontalMediaAdapter(
    private val items: List<HorizontalMediaItemModel>,
    private val onItemClickBehavior: (movieId: Int) -> Unit
) :
    RecyclerView.Adapter<HorizontalMediaAdapter.TrendingMediaViewHolder>() {

    class TrendingMediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val title: TextView = itemView.findViewById(R.id.title)
        val year: TextView = itemView.findViewById(R.id.year)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingMediaViewHolder {
        return TrendingMediaViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_horizontal_media_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TrendingMediaViewHolder, position: Int) {
        items[position].let { media ->
            Glide.with(holder.image).load(media.imageUrl).placeholder(R.drawable.placeholder).into(holder.image)

            holder.title.text = media.title
            holder.year.text = media.year
            holder.itemView.setOnClickListener {
                onItemClickBehavior.invoke(media.id)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}