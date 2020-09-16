package com.example.famouspeople.peopleList.ui.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.famouspeople.peopleList.core.useCases.GetPeopleResultUseCase
import com.example.famouspeople.peopleList.ui.modelClass.ViewPeopleResult
import kotlinx.coroutines.CoroutineScope

class PeopleDataSourceFactory(
    private val useCase: GetPeopleResultUseCase,
    private val coroutineScope: CoroutineScope,
    private val key: String
) : DataSource.Factory<Int, ViewPeopleResult>() {

    private val _sourceData = MutableLiveData<PeoplePageKeyedDataSource>()
    val sourceData: LiveData<PeoplePageKeyedDataSource> get() = _sourceData

    override fun create(): DataSource<Int, ViewPeopleResult> {
        val source = PeoplePageKeyedDataSource(
            useCase, coroutineScope, key
        )
        _sourceData.postValue(source)
        return source
    }

}