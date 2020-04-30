package com.doordash.interview.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.doordash.interview.R

/**
 * Recycler view adapter
 */
class ItemAdapter : ListAdapter<ListItem, ViewHolder>(COMPARATOR) {

    private lateinit var clickListener: AdapterListener


    fun setListener(listener: AdapterListener) {
        clickListener = listener;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener{
            var item: ListItem = getItem(holder.adapterPosition);
            clickListener.onClick(it, item)
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.unbind()
        holder.itemView.setOnClickListener(null)
        super.onViewRecycled(holder)
    }

    public interface AdapterListener {
        fun onClick(v: View, item: ListItem)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<ListItem>() {
            override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
                oldItem == newItem
        }
    }
}
