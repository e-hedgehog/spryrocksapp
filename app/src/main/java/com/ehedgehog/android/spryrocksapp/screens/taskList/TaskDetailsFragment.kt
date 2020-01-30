package com.ehedgehog.android.spryrocksapp.screens.taskList

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ehedgehog.android.spryrocksapp.R
import com.ehedgehog.android.spryrocksapp.databinding.FragmentTaskDetailsBinding
import com.ehedgehog.android.spryrocksapp.network.Task

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

        currentTask = TaskDetailsFragmentArgs.fromBundle(arguments!!).task

        currentTask?.let {
            viewModel.initTask(currentTask!!)
            viewModel.isNewTask = false
        }

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

}