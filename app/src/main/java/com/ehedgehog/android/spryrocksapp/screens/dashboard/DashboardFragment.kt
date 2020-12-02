package com.ehedgehog.android.spryrocksapp.screens.dashboard

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ehedgehog.android.spryrocksapp.R
import com.ehedgehog.android.spryrocksapp.databinding.FragmentDashboardBinding

class DashboardFragment: Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)

        binding.addEmployeeInfo.setOnClickListener {
            if (isOnline())
                findNavController().navigate(DashboardFragmentDirections.actionDashboardToEmployeeInfo())
        }

        binding.timeTracker.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.actionDashboardToTasksTracker())
        }

        return binding.root
    }

    private fun isOnline(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnected)
            return true

        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
        return false
    }

}