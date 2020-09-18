package com.example.famouspeople.di.modules

import com.example.famouspeople.features.peopleList.core.data.IPeopleDataSource
import com.example.famouspeople.features.peopleList.core.data.PeopleRepository
import com.example.famouspeople.features.personDetails.core.data.IPersonProfileImagesDataSource
import com.example.famouspeople.features.personDetails.core.data.PersonProfileRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindPeopleRepository(peopleRepository: PeopleRepository): IPeopleDataSource


    @Binds
    @Singleton
    abstract fun bindProfileRepository(profileRepository: PersonProfileRepository): IPersonProfileImagesDataSource

}