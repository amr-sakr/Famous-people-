package com.example.famouspeople.peopleList.ui

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.famouspeople.peopleList.core.domain.PeopleResult
import com.example.famouspeople.peopleList.core.useCases.GetPeopleResultUseCase
import com.example.famouspeople.peopleList.ui.modelClass.ViewPeopleResult
import com.example.famouspeople.peopleList.ui.paging.PeopleDataSourceFactory
import com.example.famouspeople.util.NetworkState
import timber.log.Timber

class PeopleViewModel(private val useCase: GetPeopleResultUseCase) : ViewModel() {


    private lateinit var dataSourceFactory: PeopleDataSourceFactory
    lateinit var peopleList: LiveData<PagedList<ViewPeopleResult>>
    lateinit var networkState: LiveData<NetworkState>
    lateinit var initialNetworkState: LiveData<NetworkState>


    fun getPeopleList(key: String) {
        Timber.d("getPeopleList called")
        val config = PagedList
            .Config
            .Builder()
            .setPageSize(1)
            .setEnablePlaceholders(false)
            .build()

        dataSourceFactory = PeopleDataSourceFactory(useCase, viewModelScope, key)
        peopleList = LivePagedListBuilder(dataSourceFactory, config).build()
        Timber.d("peopleList ${peopleList.value}")
        initialNetworkState = Transformations.switchMap(dataSourceFactory.sourceData) {
            it.initialNetworkState
        }
        Timber.d("initialNetworkState ${initialNetworkState.value}")

        networkState = Transformations.switchMap(dataSourceFactory.sourceData) {
            it.networkState
        }
        Timber.d("initialNetworkState ${networkState.value}")
    }


    fun retry() {
        dataSourceFactory.sourceData.value?.retry()
    }

    private val _peopleList1 = MutableLiveData<List<PeopleResult>>()
    val peopleList1: LiveData<List<PeopleResult>> get() = _peopleList1

    init {
//        viewModelScope.launch {
//            when (val result = useCase.invoke("6fe5d17e820e3f4b7d5f1b33e3e9f879", 1)) {
//                is Result.Success -> {
//                    Result.Success(result)
//                    _peopleList1.postValue(result.data)
//                }
//                is Result.Error -> {
//                    Result.Error(Exception())
//                }
//            }
//        }
    }
}

