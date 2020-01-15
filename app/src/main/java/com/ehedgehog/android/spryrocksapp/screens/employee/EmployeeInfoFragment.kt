package com.ehedgehog.android.spryrocksapp.screens.employee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ehedgehog.android.spryrocksapp.R
import com.ehedgehog.android.spryrocksapp.databinding.FragmentEmployeeInfoBinding

class EmployeeInfoFragment : Fragment() {

    private lateinit var binding: FragmentEmployeeInfoBinding
    private lateinit var viewModel: EmployeeInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_employee_info, container, false)
        viewModel = ViewModelProviders.of(this).get(EmployeeInfoViewModel::class.java)

        context?.let {
            Glide.with(it)
                .load(R.drawable.company_logo)
                .centerCrop()
                .fitCenter()
                .apply(RequestOptions.circleCropTransform())
                .into(binding.headerImage) }

        viewModel.loadBoardLists()

        return binding.root
    }
}