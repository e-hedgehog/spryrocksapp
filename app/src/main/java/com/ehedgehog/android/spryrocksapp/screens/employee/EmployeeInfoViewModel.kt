package com.ehedgehog.android.spryrocksapp.screens.employee

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ehedgehog.android.spryrocksapp.Application
import com.ehedgehog.android.spryrocksapp.BuildConfig
import com.ehedgehog.android.spryrocksapp.network.TrelloApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class EmployeeInfoViewModel : ViewModel() {

    @Inject
    lateinit var apiService: TrelloApiService

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        Application.appComponent.injectEmpoyeeInfoViewModel(this)
    }

    fun loadBoardLists() {
        coroutineScope.launch {
            val listsDeferred = apiService.getBoardLists(BuildConfig.BOARD_ID)

            val listsResult = listsDeferred.await()
            Log.i("AAAAAA", listsResult.toString())
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}