package com.example.famouspeople.peopleList.ui.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.famouspeople.networking.Result
import com.example.famouspeople.peopleList.core.useCases.GetPeopleResultUseCase
import com.example.famouspeople.peopleList.ui.modelClass.ViewPeopleResult
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
    PageKeyedDataSource<Int, ViewPeopleResult>() {

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
        callback: LoadInitialCallback<Int, ViewPeopleResult>
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
                    val error = NetworkState.error(result.exception.message)
                    Timber.d("in error2 ${result.exception.message}")
                    _networkState.postValue(error)
                    _initialNetworkState.postValue(error)
                }
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, ViewPeopleResult>
    ) {
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ViewPeopleResult>) {
        _networkState.postValue(NetworkState.LOADING)
        coroutineScope.launch {
            when (val result = useCase.invoke(key, 1)) {
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
                    _networkState.postValue(NetworkState.error(result.exception.message))
                }
            }
        }
    }
}