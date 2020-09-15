package com.example.famouspeople.peopleList.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.famouspeople.peopleList.core.useCases.GetPeopleResultUseCase


@Suppress("UNCHECKED_CAST")
class PeopleViewModelFactory(private val useCase: GetPeopleResultUseCase) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PeopleViewModel(useCase) as T
    }


}