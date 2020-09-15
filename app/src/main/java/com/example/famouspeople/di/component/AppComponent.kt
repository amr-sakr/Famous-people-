package com.example.famouspeople.di.component

import com.example.famouspeople.MainActivity
import com.example.famouspeople.peopleList.ui.PeopleFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent {


    fun inject(mainActivity: MainActivity)
    fun inject(peopleFragment: PeopleFragment)

//
//    @Component.Factory
//    interface Factory {
//        fun getPeopleDataSource(@BindsInstance repo: IPeopleDataSource): AppComponent
//    }

}