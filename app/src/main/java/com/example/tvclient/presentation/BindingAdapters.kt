package com.example.tvclient.presentation

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
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
