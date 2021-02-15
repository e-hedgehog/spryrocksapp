package com.ehedgehog.android.spryrocksapp.screens.taskList

import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
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

class TasksAdapter(
    private val timerManager: TaskTimerManager,
    private val clickListener: (task: Task) -> Unit,
    private val contextMenuItemClickListener: (item: MenuItem, task: Task) -> Boolean
) : ListAdapter<Task, TasksAdapter.TaskViewHolder>(DiffCallback) {

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
        holder.itemView.setOnClickListener { clickListener(task) }
        holder.itemView.setOnLongClickListener {
            val popupMenu = PopupMenu(holder.itemView.context, holder.itemView, Gravity.END)
            popupMenu.inflate(R.menu.context_menu_tasks_tracker)
            popupMenu.setOnMenuItemClickListener { contextMenuItemClickListener(it, task) }
            popupMenu.show()
            true
        }
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

}