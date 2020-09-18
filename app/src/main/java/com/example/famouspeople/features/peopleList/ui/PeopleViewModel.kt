package com.example.famouspeople.features.peopleList.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.famouspeople.features.peopleList.core.useCases.GetPeopleResultUseCase
import com.example.famouspeople.features.peopleList.ui.modelClass.ViewPerson
import com.example.famouspeople.features.peopleList.ui.paging.PeopleDataSourceFactory
import com.example.famouspeople.util.NetworkState
import timber.log.Timber
import javax.inject.Inject

class PeopleViewModel @Inject constructor(private val useCase: GetPeopleResultUseCase) :
    ViewModel() {


    private lateinit var dataSourceFactory: PeopleDataSourceFactory
    lateinit var peopleList: LiveData<PagedList<ViewPerson>>
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

}

