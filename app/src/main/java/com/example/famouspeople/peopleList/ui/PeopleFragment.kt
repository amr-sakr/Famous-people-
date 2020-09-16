package com.example.famouspeople.peopleList.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.famouspeople.databinding.PeopleFragmentBinding
import com.example.famouspeople.peopleList.core.data.PeopleRepository
import com.example.famouspeople.peopleList.core.useCases.GetPeopleResultUseCase
import com.example.famouspeople.networking.NetworkConnectionInterceptor
import com.example.famouspeople.networking.remoteDataSource.WebService
import com.example.famouspeople.peopleList.ui.modelClass.ViewPeopleResult
import com.example.famouspeople.util.Status
import okhttp3.logging.HttpLoggingInterceptor

class PeopleFragment : Fragment() {


    private var _binding: PeopleFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PeopleViewModel
    private lateinit var peopleAdapter: PeoplePagedListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PeopleFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    private val apiKey = "6fe5d17e820e3f4b7d5f1b33e3e9f879"

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //using manual DI for now to make sure if call API is working
        val networkInterceptor = NetworkConnectionInterceptor(activity!!)
        val logging = HttpLoggingInterceptor()
        val webService = WebService.invoke(networkInterceptor, logging)
        val repo = PeopleRepository(webService)
        val useCase = GetPeopleResultUseCase(repo)
        val factory = PeopleViewModelFactory(useCase)

        viewModel = ViewModelProvider(this, factory).get(PeopleViewModel::class.java)
        viewModel.getPeopleList(apiKey)
        initRecyclerView()
        observeNetworkState()
        observeInitialNetworkState()
        observePeopleList()

    }

    private fun initRecyclerView() {
        binding.rvPeople.apply {
            layoutManager = LinearLayoutManager(context)
            peopleAdapter = PeoplePagedListAdapter(
                object : PeoplePagedListAdapter.OnPeopleClickListener {
                    override fun onItemClick(item: ViewPeopleResult) {
                        Toast.makeText(requireContext(), "clicked", Toast.LENGTH_SHORT).show();
                    }

                }) { viewModel.retry() }
            adapter = peopleAdapter
        }
    }

    private fun observePeopleList() {
        viewModel.peopleList.observe(viewLifecycleOwner, {

            if (!it.isEmpty()) {
                peopleAdapter.submitList(it)
            } else {
                binding.errorMessage.text = "No Data Available"
            }
        })
    }

    private fun observeNetworkState() {
        viewModel.networkState.observe(viewLifecycleOwner, {
            peopleAdapter.setNetworkState(it)
        })
    }

    @SuppressLint("SetTextI18n")
    private fun observeInitialNetworkState() {
        viewModel.initialNetworkState.observe(viewLifecycleOwner, { networkState ->
            when (networkState.status) {
                Status.RUNNING -> {
                    binding.rvPeople.visibility = View.GONE
                    binding.loading.visibility = View.VISIBLE
                    binding.errorMessage.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    binding.rvPeople.visibility = View.VISIBLE
                    binding.loading.visibility = View.GONE
                    binding.errorMessage.visibility = View.GONE
                }
                Status.FAILED -> {
                    binding.rvPeople.visibility = View.GONE
                    binding.loading.visibility = View.GONE
                    binding.errorMessage.visibility = View.VISIBLE
                    networkState.errorMessageRes?.let {
                        binding.errorMessage.text = "Something went wrong"
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}