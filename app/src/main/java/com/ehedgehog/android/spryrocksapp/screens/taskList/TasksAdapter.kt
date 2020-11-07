package com.ehedgehog.android.spryrocksapp.screens.taskList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ehedgehog.android.spryrocksapp.R
import com.ehedgehog.android.spryrocksapp.databinding.ItemListTaskBinding
import com.ehedgehog.android.spryrocksapp.network.Task
import com.ehedgehog.android.spryrocksapp.screens.TaskTimerManager

class TasksAdapter(private val timerManager: TaskTimerManager, private val onClickListener: OnClickListener) : ListAdapter<Task, TasksAdapter.TaskViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_list_task,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.itemView.setOnClickListener { onClickListener.onClick(task) }
        holder.bind(TaskListItemModel(timerManager.time, task))
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class TaskViewHolder(private val binding: ItemListTaskBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(model: TaskListItemModel) {
            binding.model = model
            binding.executePendingBindings()
            if (model.task.isStarted) {
                model.timeLiveData.observe(binding.root.context as LifecycleOwner, Observer {
                    binding.taskTimeField.text = it.toString()
                })
            }
        }
    }

    class OnClickListener(val clickListener: (task: Task) -> Unit) {
        fun onClick(task: Task) = clickListener(task)
    }

}