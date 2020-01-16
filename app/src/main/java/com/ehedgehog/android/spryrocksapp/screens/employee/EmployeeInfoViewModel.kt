package com.ehedgehog.android.spryrocksapp.screens.employee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ehedgehog.android.spryrocksapp.Application
import com.ehedgehog.android.spryrocksapp.BuildConfig
import com.ehedgehog.android.spryrocksapp.network.BoardList
import com.ehedgehog.android.spryrocksapp.network.EmployeeInfo
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

    private val _lists = MutableLiveData<List<BoardList>>()
    val lists: LiveData<List<BoardList>> get() = _lists

    private val _eventInfoSent = MutableLiveData<Boolean>()
    val eventInfoSent:LiveData<Boolean> get() = _eventInfoSent

    init {
        Application.appComponent.injectEmpoyeeInfoViewModel(this)
    }

    fun loadBoardLists() {
        coroutineScope.launch {
            val listsDeferred = apiService.getBoardLists(BuildConfig.BOARD_ID)

            val listsResult = listsDeferred.await()
            _lists.value = listsResult
        }
    }

    fun createCardInBoardList(employeeInfo: EmployeeInfo) {
        coroutineScope.launch {
            val cardsDeferred = apiService.createCardInBoardList(
                lists.value!![0].id,
                employeeInfo.name,
                employeeInfo.toString()
            )

            cardsDeferred.await()
            _eventInfoSent.value = true
        }
    }

    fun onInfoSendComplete() {
        _eventInfoSent.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}