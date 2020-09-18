package com.example.famouspeople.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.famouspeople.di.viewModelInjecttionUtil.ViewModelFactory
import com.example.famouspeople.di.annotation.ViewModelKey
import com.example.famouspeople.features.peopleList.ui.PeopleViewModel
import com.example.famouspeople.features.personDetails.ui.PersonDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PeopleViewModel::class)
    abstract fun peopleViewModel(viewModel: PeopleViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(PersonDetailsViewModel::class)
    abstract fun personDetailsViewModel(viewModel: PersonDetailsViewModel): ViewModel
}