package com.example.famouspeople.peopleList.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.famouspeople.R
import com.example.famouspeople.peopleList.core.data.PeopleRepository
import com.example.famouspeople.peopleList.core.useCases.GetPeopleResultUseCase
import com.example.famouspeople.networking.NetworkConnectionInterceptor
import com.example.famouspeople.networking.remoteDataSource.WebService

class PeopleFragment : Fragment() {


    private lateinit var viewModel: PeopleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.people_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //using manual DI for now to make sure if call API is working
        val networkInterceptor = NetworkConnectionInterceptor(activity!!)
        val webService = WebService.invoke(networkInterceptor)
        val repo = PeopleRepository(webService)
        val useCase = GetPeopleResultUseCase(repo)
        val factory = PeopleViewModelFactory(useCase)
        viewModel = ViewModelProvider(this, factory).get(PeopleViewModel::class.java)

        viewModel.peopleList.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it.size.toString(), Toast.LENGTH_SHORT).show();
        })
    }

}