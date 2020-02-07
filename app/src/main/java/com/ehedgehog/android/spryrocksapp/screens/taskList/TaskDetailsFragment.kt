package com.ehedgehog.android.spryrocksapp.screens.taskList

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ehedgehog.android.spryrocksapp.R
import com.ehedgehog.android.spryrocksapp.databinding.FragmentTaskDetailsBinding
import com.ehedgehog.android.spryrocksapp.network.Task
import java.text.DecimalFormat
import java.util.*

class TaskDetailsFragment : Fragment() {

    private lateinit var binding: FragmentTaskDetailsBinding
    private lateinit var viewModel: TaskDetailsViewModel

    private var currentTask: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task_details, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProviders.of(this).get(TaskDetailsViewModel::class.java)
        binding.viewModel = viewModel

        val currentTaskId = TaskDetailsFragmentArgs.fromBundle(arguments!!).taskId
        if (currentTaskId != -1)
            currentTask = viewModel.getTaskById(currentTaskId)

        currentTask?.let {
            viewModel.initTask(it)

            if (it.isStarted) {
                binding.stopButton.visibility = View.VISIBLE
                binding.startButton.visibility = View.GONE
                val currentTime = Date().time
                val interval = (currentTime - it.lastPause) / 1000
                viewModel.startTimer(it.time, interval)
            }

            viewModel.isNewTask = false
            binding.timerContainer.visibility = View.VISIBLE
        }

        binding.startButton.setOnClickListener {
            currentTask?.let {task ->
                task.isStarted = true
                viewModel.updateCurrentTask(task)
            }

            it.visibility = View.GONE
            binding.stopButton.visibility = View.VISIBLE
            if (currentTask != null)
                viewModel.startTimer(currentTask!!.time, 0)
            else viewModel.startTimer(null, null)

        }

        binding.stopButton.setOnClickListener {
            currentTask?.let {task ->
                task.isStarted = false
                task.time = viewModel.time.value
                task.lastPause = 0
                viewModel.updateCurrentTask(task)
            }
            it.visibility = View.GONE
            binding.startButton.visibility = View.VISIBLE
            viewModel.stopTimer()
        }

        binding.resetButton.setOnClickListener {
            currentTask?.let {task ->
                viewModel.resetCurrentTask(task)
            }

            binding.stopButton.visibility = View.GONE
            binding.startButton.visibility = View.VISIBLE
            viewModel.stopTimer()
        }

        viewModel.time.observe(this, Observer {
            if (it != null) {
                val formatter = DecimalFormat("00")
                val hoursString = it.hours.toString()
                val minutesString = formatter.format(it.minutes)
                val secondsString = formatter.format(it.seconds)
                binding.taskTimer.text =
                    getString(R.string.timer_text, hoursString, minutesString, secondsString)
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_task_details, menu)

        val createItem = menu.findItem(R.id.menu_create_task)
        createItem.setOnMenuItemClickListener {
            if (viewModel.taskDescription.value.isNullOrEmpty()) {
                Toast.makeText(context, "Task description required", Toast.LENGTH_SHORT).show()
            } else {
                if (viewModel.isNewTask)
                    viewModel.saveTask()
                else
                    currentTask?.let { task -> viewModel.updateCurrentTask(task) }
                findNavController().navigateUp()
            }
            true
        }
    }

    override fun onStop() {
        super.onStop()
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onPause() {
        super.onPause()
        currentTask?.let {
            if (it.isStarted) {
                viewModel.stopTimer()
                it.time = viewModel.time.value
                it.lastPause = Date().time
                viewModel.updateCurrentTask(it)
            }
        }
    }

}