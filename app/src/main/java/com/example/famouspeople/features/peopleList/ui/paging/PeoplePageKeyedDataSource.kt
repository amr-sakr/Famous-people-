package com.example.famouspeople.features.peopleList.ui.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.famouspeople.networking.Result
import com.example.famouspeople.features.peopleList.core.useCases.GetPeopleResultUseCase
import com.example.famouspeople.features.peopleList.ui.modelClass.ViewPerson
import com.example.famouspeople.util.NetworkState
import com.example.famouspeople.util.toViewPeopleResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class PeoplePageKeyedDataSource(
    private val useCase: GetPeopleResultUseCase,
    private val coroutineScope: CoroutineScope,
    private val key: String
) :
    PageKeyedDataSource<Int, ViewPerson>() {

    private var retry: (() -> Any)? = null

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState> get() = _networkState

    private val _initialNetworkState = MutableLiveData<NetworkState>()
    val initialNetworkState: LiveData<NetworkState> get() = _initialNetworkState

    fun retry() {
        retry?.invoke()
    }


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ViewPerson>
    ) {

        runBlocking {
            Timber.d("in coroutineScope")
            when (val result = useCase.invoke(key, 1)) {
                is Result.Success -> {
                    retry = null
                    _networkState.postValue(NetworkState.LOADED)
                    _initialNetworkState.postValue(NetworkState.LOADED)
                    callback.onResult(result.data?.map {
                        it.toViewPeopleResult()
                    }!!, null, 2)
                    Timber.d("data is ${result.data}")
                }
                is Result.Error -> {
                    Timber.d("in error")
                    retry = {
                        loadInitial(params, callback)
                    }
                    val error = NetworkState.error(null, result.exception.message)
                    Timber.d("in error2 ${result.exception.message}")
                    _networkState.postValue(error)
                    _initialNetworkState.postValue(error)
                }
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, ViewPerson>
    ) {
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ViewPerson>) {
        _networkState.postValue(NetworkState.LOADING)
        coroutineScope.launch {
            when (val result = useCase.invoke(key, params.key + 1)) {
                is Result.Success -> {
                    retry = null
                    callback.onResult(result.data?.map {
                        it.toViewPeopleResult()
                    }!!, params.key + 1)
                    _networkState.postValue(NetworkState.LOADED)
                }

                is Result.Error -> {
                    retry = {
                        loadAfter(params, callback)
                    }
                    _networkState.postValue(NetworkState.error(null, result.exception.message))
                }
            }
        }
    }
}