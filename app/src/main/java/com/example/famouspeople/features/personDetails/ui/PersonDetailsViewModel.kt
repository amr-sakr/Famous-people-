package com.example.famouspeople.features.personDetails.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.famouspeople.features.personDetails.core.useCases.GetProfileImagesUseCase
import com.example.famouspeople.features.personDetails.ui.modelClass.ViewProfile
import com.example.famouspeople.networking.Result
import com.example.famouspeople.util.toViewProfile
import kotlinx.coroutines.launch
import javax.inject.Inject

class PersonDetailsViewModel @Inject constructor(private val useCase: GetProfileImagesUseCase) :
    ViewModel() {

    private val _profileImagesList = MutableLiveData<List<ViewProfile>>()
    val profileImagesList: LiveData<List<ViewProfile>> get() = _profileImagesList


    fun getProfileImages(personId: Int, apiKey: String) {
        viewModelScope.launch {

            when (val result = useCase.invoke(personId, apiKey)) {
                is Result.Success -> {
                    val data = result.data?.map {
                        it.toViewProfile()
                    }
                    _profileImagesList.postValue(data)
                }

                is Result.Error -> result.exception

                else -> {
                    Result.Error(Exception())
                }
            }
        }
    }
}