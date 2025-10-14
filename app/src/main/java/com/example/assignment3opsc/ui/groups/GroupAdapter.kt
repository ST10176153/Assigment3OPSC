package com.example.assignment3opsc.ui.groups


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment3opsc.R
import com.example.assignment3opsc.data.group.GroupEntity

class GroupAdapter(
    private val onClick: (GroupEntity) -> Unit,
    private val onLongPress: (GroupEntity) -> Unit
) : RecyclerView.Adapter<GroupAdapter.VH>() {

    private val items = mutableListOf<GroupEntity>()

    fun submit(list: List<GroupEntity>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_group_card, parent, false)
        return VH(v)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position], onClick, onLongPress)
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvEmoji: TextView = itemView.findViewById(R.id.tvEmoji)
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        fun bind(item: GroupEntity, onClick: (GroupEntity) -> Unit, onLong: (GroupEntity) -> Unit) {
            tvEmoji.text = when {
                item.emoji.isNotBlank() -> item.emoji
                else -> "🎬"
            }
            tvName.text = item.name
            itemView.setOnClickListener { onClick(item) }
            itemView.setOnLongClickListener { onLong(item); true }
        }
    }
}
