package com.example.assignment3opsc.ui.groups


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.assignment3opsc.R

data class TrendingItem(val posterUrl: String)

class TrendingAdapter : RecyclerView.Adapter<TrendingAdapter.VH>() {
    private val items = mutableListOf<TrendingItem>()
    fun submit(list: List<TrendingItem>) { items.apply { clear(); addAll(list) }; notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_trending_poster, parent, false)
        return VH(v)
    }
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val img: ImageView = itemView.findViewById(R.id.imgPoster)
        fun bind(item: TrendingItem) { img.load(item.posterUrl) }
    }
}
