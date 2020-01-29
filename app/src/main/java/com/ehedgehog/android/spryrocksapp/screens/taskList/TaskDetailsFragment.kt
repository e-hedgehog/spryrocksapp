package com.ehedgehog.android.spryrocksapp.screens.taskList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ehedgehog.android.spryrocksapp.R
import com.ehedgehog.android.spryrocksapp.databinding.FragmentTaskDetailsBinding

class TaskDetailsFragment : Fragment() {

    private lateinit var binding: FragmentTaskDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task_details, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

}