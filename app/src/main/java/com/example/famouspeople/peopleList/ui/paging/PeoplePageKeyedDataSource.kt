package com.example.famouspeople.peopleList.ui.paging

import androidx.paging.PageKeyedDataSource
import com.example.famouspeople.peopleList.core.useCases.GetPeopleResultUseCase
import com.example.famouspeople.peopleList.ui.ModelClass.ViewPeopleResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PeoplePageKeyedDataSource(
    private val useCase: GetPeopleResultUseCase,
    private val coroutineScope: CoroutineScope
) :
    PageKeyedDataSource<Int, ViewPeopleResult>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ViewPeopleResult>
    ) {

        coroutineScope.launch {
//            try {
//                val response = useCase.invoke()
//            } catch (e: ApiException) {
//
//            } catch (e: Exception) {
//
//            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, ViewPeopleResult>
    ) {
        TODO("Not yet implemented")
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ViewPeopleResult>) {
        TODO("Not yet implemented")
    }
}