package com.doordash.interview.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.doordash.interview.R
import kotlinx.android.synthetic.main.item.view.*

/**
 * View that represents each row within RecyclerView
 */
class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val name = itemView.name
    private val image = itemView.image
    private val description = itemView.description
    private val status = itemView.status


    fun bind(row: ListItem) {
        name.setText(row.name)
        description.setText(row.description)
        status.setText(row.status)
        Glide.with(itemView.context).load(row.logo)
//            .apply(RequestOptions().override(150, 100))
            .placeholder(R.drawable.ic_imageplaceholder_icon)
            .error(R.drawable.ic_imageplaceholder_icon)
            .into(image);
    }

    fun unbind() {
        Glide.with(itemView.context).clear(image);
    }
}