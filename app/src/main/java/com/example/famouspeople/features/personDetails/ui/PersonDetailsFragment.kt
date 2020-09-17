package com.example.famouspeople.features.personDetails.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.famouspeople.R
import com.example.famouspeople.databinding.PersonDetailsFragmentBinding
import com.example.famouspeople.features.peopleList.ui.modelClass.ViewPerson
import com.example.famouspeople.features.personDetails.core.data.PersonProfileRepository
import com.example.famouspeople.features.personDetails.core.useCases.GetProfileImagesUseCase
import com.example.famouspeople.features.personDetails.ui.modelClass.ViewProfile
import com.example.famouspeople.networking.NetworkConnectionInterceptor
import com.example.famouspeople.networking.remoteDataSource.WebService
import com.example.famouspeople.util.*
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

class PersonDetailsFragment : Fragment() {

    private var _binding: PersonDetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PersonDetailsViewModel
    private lateinit var profileAdapter: PersonProfilesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PersonDetailsFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = PersonDetailsFragmentArgs.fromBundle(requireArguments())
        val person = args.viewPerson

        val networkInterceptor = NetworkConnectionInterceptor(requireContext())
        val logging = HttpLoggingInterceptor()
        val webService = WebService.invoke(networkInterceptor, logging)
        val repo = PersonProfileRepository(webService)
        val useCase = GetProfileImagesUseCase(repo)
        val factory = PersonDetailsViewModelFactory(useCase)
        viewModel = ViewModelProvider(this, factory).get(PersonDetailsViewModel::class.java)

        viewModel.getProfileImages(person.id!!, API_KEY)
        bindView(person)
        initRecyclerView()
        observeProfile()
    }


    private fun observeProfile() {
        viewModel.profileImagesList.observe(viewLifecycleOwner, {
            if (it != null && it.isNotEmpty()) {

                profileAdapter.submitList(it)
            }

        })
    }


    private fun initRecyclerView() {
        binding.rvImages.apply {
            profileAdapter = PersonProfilesListAdapter(object :
                PersonProfilesListAdapter.OnProfileClickListener {
                override fun onClick(item: ViewProfile) {
                    Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
                    Timber.d("image URL $IMAGES_URL${item.filePath} ")
                }

            })
            layoutManager = GridLayoutManager(requireContext(), 4)
            adapter = profileAdapter

        }
    }

    private fun bindView(person: ViewPerson) {
        person.apply {
            binding.name.text = name
            if (gender == 1) {
                binding.gender.text = getString(R.string.gender_female)
            } else {
                binding.gender.text = getString(R.string.gender_male)
            }

            binding.popularity.text = popularity.toString()
            binding.department.text = knownForDepartment

            Glide.with(requireContext())
                .load(IMAGES_URL + profilePath)
                .apply(
                    RequestOptions()
                        .circleCrop()
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .error(R.drawable.ic_baseline_person_24)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(binding.ivProfile)


        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}