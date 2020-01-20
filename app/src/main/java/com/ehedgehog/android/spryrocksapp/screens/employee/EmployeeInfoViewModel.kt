package com.ehedgehog.android.spryrocksapp.screens.employee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ehedgehog.android.spryrocksapp.Application
import com.ehedgehog.android.spryrocksapp.BuildConfig
import com.ehedgehog.android.spryrocksapp.network.BoardList
import com.ehedgehog.android.spryrocksapp.network.EmployeeInfo
import com.ehedgehog.android.spryrocksapp.network.TrelloApiService
import com.ehedgehog.android.spryrocksapp.screens.DatabaseManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class EmployeeInfoViewModel : ViewModel() {

    @Inject
    lateinit var apiService: TrelloApiService

    @Inject
    lateinit var databaseManager: DatabaseManager

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _lists = MutableLiveData<List<BoardList>>()
    val lists: LiveData<List<BoardList>> get() = _lists

    private val _cardId = MutableLiveData<String>()
    val cardId: LiveData<String> get() = _cardId

    private val _eventInfoSent = MutableLiveData<Boolean>()
    val eventInfoSent:LiveData<Boolean> get() = _eventInfoSent

    private val _storedEmployeeInfo = MutableLiveData<EmployeeInfo>()
    val storedEmployeeInfo: LiveData<EmployeeInfo> get() = _storedEmployeeInfo

    init {
        Application.appComponent.injectEmpoyeeInfoViewModel(this)
    }

    fun loadBoardLists() {
        coroutineScope.launch {
            val listsDeferred = apiService.getBoardLists(BuildConfig.BOARD_ID)

            val listsResult = listsDeferred.await()
            _lists.value = listsResult

            if (listsResult.isNullOrEmpty())
                createDefaultListInBoard()
        }
    }

    fun updateEmployeeInfoIfStored() {
        coroutineScope.launch {
            _storedEmployeeInfo.value = databaseManager.getCurrentEmployee()
        }
    }

    private suspend fun createDefaultListInBoard(): BoardList {
        val createListDeferred = apiService.createListInBoard(BuildConfig.BOARD_ID, DEFAULT_LIST_NAME)
        val boardList = createListDeferred.await()
        _lists.value = listOf(boardList)
        return boardList
    }

    fun createCardInBoardList(employeeInfo: EmployeeInfo, cardId: String?) {
        coroutineScope.launch {
            var employeeList = findEmployeeList()

            if (employeeList == null) {
                employeeList = createDefaultListInBoard()
            }

            val cardsDeferred = employeeInfo.name?.let {
                if (cardId == null)
                    apiService.createCardInBoardList(employeeList.id, it, employeeInfo.toString())
                else
                    apiService.updateCardInBoardList(cardId, it, employeeInfo.toString())
            }

            databaseManager.saveCurrentEmployee(employeeInfo)
            _cardId.value = cardsDeferred?.await()?.id
            _eventInfoSent.value = true

        }
    }

    private fun findEmployeeList(): BoardList? {
        for (list in _lists.value!!.iterator())
            if (list.name == DEFAULT_LIST_NAME)
                return list

        return null
    }

    fun onInfoSendComplete() {
        _eventInfoSent.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    companion object {
        private const val DEFAULT_LIST_NAME = "Employees"
    }
}