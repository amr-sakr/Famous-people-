package com.example.famouspeople.features.peopleList.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.famouspeople.R
import com.example.famouspeople.application.PeopleApplication
import com.example.famouspeople.databinding.PeopleFragmentBinding
import com.example.famouspeople.di.viewModelInjecttionUtil.ViewModelFactory
import com.example.famouspeople.features.peopleList.ui.modelClass.ViewPerson
import com.example.famouspeople.util.API_KEY
import com.example.famouspeople.util.Status
import com.example.famouspeople.util.hide
import com.example.famouspeople.util.show
import javax.inject.Inject

class PeopleFragment : Fragment() {

    private var _binding: PeopleFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PeopleViewModel
    private lateinit var peopleAdapter: PeoplePagedListAdapter

    @Inject
    lateinit var factory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, factory)[PeopleViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PeopleFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getPeopleList(API_KEY)
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
                    override fun onItemClick(item: ViewPerson) {
                        binding.root.findNavController().navigate(
                            PeopleFragmentDirections.actionPeopleFragmentToPersonDetailsFragment(
                                item
                            )
                        )
                    }

                }) { viewModel.retry() }
            peopleAdapter
            adapter = peopleAdapter
        }
    }


    private fun observePeopleList() {
        viewModel.peopleList.observe(viewLifecycleOwner, {

            if (!it.isEmpty()) {
                peopleAdapter.submitList(it)
            } else {
                binding.errorMessage.text = getString(R.string.no_data_available_message)
            }
        })
    }

    private fun observeNetworkState() {
        viewModel.networkState.observe(viewLifecycleOwner, {
            peopleAdapter.setNetworkState(it)
        })
    }


    private fun observeInitialNetworkState() {
        viewModel.initialNetworkState.observe(viewLifecycleOwner, { networkState ->
            when (networkState.status) {
                Status.RUNNING -> {
                    binding.rvPeople.hide()
                    binding.loading.show()
                    binding.errorMessage.hide()
                }
                Status.SUCCESS -> {
                    binding.rvPeople.show()
                    binding.loading.hide()
                    binding.errorMessage.hide()
                }
                Status.FAILED -> {
                    binding.rvPeople.hide()
                    binding.loading.hide()
                    binding.errorMessage.show()
                    networkState.errorMessageRes?.let {
                        binding.errorMessage.text =
                            getString(R.string.somethine_went_wrong_error_message)
                    }
                }
            }
        })
    }

    override fun onAttach(context: Context) {
        (context.applicationContext as PeopleApplication)
            .appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}