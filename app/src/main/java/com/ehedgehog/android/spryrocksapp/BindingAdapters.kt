package com.ehedgehog.android.spryrocksapp

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ehedgehog.android.spryrocksapp.network.Task
import com.ehedgehog.android.spryrocksapp.screens.taskList.TasksAdapter

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Task>?) {
    val adapter = recyclerView.adapter as TasksAdapter
    adapter.submitList(data)
}
