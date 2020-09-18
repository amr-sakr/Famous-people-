package com.example.famouspeople.di.component

import android.app.Application
import android.net.ConnectivityManager
import com.example.famouspeople.MainActivity
import com.example.famouspeople.di.modules.AppModule
import com.example.famouspeople.di.modules.NetworkModule
import com.example.famouspeople.di.modules.ViewModelModule
import com.example.famouspeople.features.peopleList.ui.PeopleFragment
import com.example.famouspeople.features.personDetails.ui.PersonDetailsFragment
import com.example.famouspeople.networking.NetworkConnectionInterceptor
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, AppModule::class, ViewModelModule::class])
interface AppComponent {


    fun inject(mainActivity: MainActivity)
    fun inject(peopleFragment: PeopleFragment)
    fun inject(detailsFragment: PersonDetailsFragment)


    @Component.Factory
    interface Factory {
        fun create(
            networkModule: NetworkModule,
            @BindsInstance application: Application,
            @BindsInstance networkConnectionInterceptor: NetworkConnectionInterceptor
        ): AppComponent
    }

}