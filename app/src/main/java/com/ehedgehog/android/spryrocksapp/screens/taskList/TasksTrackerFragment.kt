package com.ehedgehog.android.spryrocksapp.screens.taskList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ehedgehog.android.spryrocksapp.R
import com.ehedgehog.android.spryrocksapp.databinding.FragmentTasksTrackerBinding

class TasksTrackerFragment: Fragment() {

    private lateinit var binding: FragmentTasksTrackerBinding
    private lateinit var viewModel: TasksTrackerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks_tracker, container, false)
        viewModel = ViewModelProviders.of(this).get(TasksTrackerViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.trackerRecyclerView.adapter = TasksAdapter(TasksAdapter.OnClickListener {
            viewModel.displayTaskDetails(it.id)
        })

        viewModel.navigateToSelectedTaskById.observe(this, Observer {taskId ->
            if (taskId != null) {
                findNavController().navigate(TasksTrackerFragmentDirections.actionTasksTrackerToTaskDetails(taskId))
                viewModel.displayTaskDetailsComplete()
            }
        })

        viewModel.loadStoredTasks()
        viewModel.initCurrentTask()

        return binding.root
    }

}