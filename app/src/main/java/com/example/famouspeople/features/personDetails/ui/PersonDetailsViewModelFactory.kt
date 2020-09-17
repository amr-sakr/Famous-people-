package com.example.famouspeople.features.personDetails.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.famouspeople.features.personDetails.core.useCases.GetProfileImagesUseCase


@Suppress("UNCHECKED_CAST")
class PersonDetailsViewModelFactory(private val useCase: GetProfileImagesUseCase) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PersonDetailsViewModel(useCase) as T
    }


}