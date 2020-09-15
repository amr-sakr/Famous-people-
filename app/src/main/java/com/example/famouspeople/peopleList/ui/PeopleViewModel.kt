package com.example.famouspeople.peopleList.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.famouspeople.peopleList.core.domain.PeopleResult
import com.example.famouspeople.peopleList.core.useCases.GetPeopleResultUseCase
import com.example.famouspeople.util.ApiException
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.RuntimeException

class PeopleViewModel(private val useCase: GetPeopleResultUseCase) : ViewModel() {

    private val _peopleList = MutableLiveData<List<PeopleResult>>()
    val peopleList: LiveData<List<PeopleResult>> get() = _peopleList

    init {
        //TODO remove after implementing paging
        viewModelScope.launch {
            try {
                val response = useCase.invoke("6fe5d17e820e3f4b7d5f1b33e3e9f879", 1)
                response.let {
                    _peopleList.postValue(it)
                }

            } catch (e: ApiException) {
                Timber.d("ApiException ${e.message}")
            } catch (e: RuntimeException) {
                Timber.d("RuntimeException ${e.message}")
            }
        }


    }
}

