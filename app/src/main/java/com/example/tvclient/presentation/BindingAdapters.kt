package com.example.tvclient.presentation

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.tvclient.domain.ChannelCategory

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<ChannelCategory>?) {
    val adapter = recyclerView.adapter as TVListAdapter
    adapter.submitList(data)
}

@BindingAdapter("isVisible")
fun progressVisibility(progress: ProgressBar, isVisible: Boolean) {
    progress.visibility = if(isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("divider")
fun setRecyclerViewDivider(recyclerView: RecyclerView, drawable: Drawable) {
    val itemDecoration = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
    itemDecoration.setDrawable(drawable)
    recyclerView.addItemDecoration(itemDecoration)
}