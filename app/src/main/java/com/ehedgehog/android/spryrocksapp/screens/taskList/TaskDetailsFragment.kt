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
import java.text.DecimalFormat

class TaskDetailsFragment : Fragment() {

    private lateinit var binding: FragmentTaskDetailsBinding
    private lateinit var viewModel: TaskDetailsViewModel

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
            viewModel.initTaskById(currentTaskId)

        viewModel.currentTask.observe(this, Observer {
            viewModel.onInitializeTask(it)
        })

        viewModel.timerManager.time.observe(this, Observer {
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
            when {
                viewModel.projectName.value.isNullOrEmpty() -> Toast.makeText(context, "Project name required", Toast.LENGTH_SHORT).show()
                viewModel.taskDescription.value.isNullOrEmpty() -> Toast.makeText(context, "Task description required", Toast.LENGTH_SHORT).show()
                else -> {
                    viewModel.onSaveCurrentTask()
                    findNavController().navigateUp()
                }
            }
            true
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.unpauseTimerIfStarted()
    }

    override fun onStop() {
        super.onStop()
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)

        viewModel.onPauseTimer()
    }

}