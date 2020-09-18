package com.example.famouspeople.features.personDetails.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.famouspeople.application.PeopleApplication
import com.example.famouspeople.R
import com.example.famouspeople.databinding.PersonDetailsFragmentBinding
import com.example.famouspeople.di.viewModelInjecttionUtil.ViewModelFactory
import com.example.famouspeople.features.peopleList.ui.modelClass.ViewPerson
import com.example.famouspeople.features.personDetails.ui.modelClass.ViewProfile
import com.example.famouspeople.util.API_KEY
import com.example.famouspeople.util.EventObserver
import com.example.famouspeople.util.IMAGES_URL
import javax.inject.Inject

class PersonDetailsFragment : Fragment(), PersonProfilesListAdapter.OnProfileClickListener {

    private var _binding: PersonDetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PersonDetailsViewModel
    private lateinit var profileAdapter: PersonProfilesListAdapter

    @Inject
    lateinit var factory: ViewModelFactory

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


        viewModel = ViewModelProvider(this, factory)[PersonDetailsViewModel::class.java]

        viewModel.getProfileImages(person.id!!, API_KEY)
        bindView(person)
        initRecyclerView()
        observeProfile()
        observeNavigateToViewerEvent()
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
                    val fullImagePath = "$IMAGES_URL${item.filePath}"
                    viewModel.getImagePath(fullImagePath)
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


    private fun observeNavigateToViewerEvent() {
        viewModel.navigateToPhotoViewerEvent.observe(viewLifecycleOwner, EventObserver {
            val action =
                PersonDetailsFragmentDirections.actionPersonDetailsFragmentToPhotoViewerFragment(
                    viewModel.imagePath
                )
            findNavController().navigate(action)
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

    override fun onClick(item: ViewProfile) {
        val fullImagePath = "$IMAGES_URL${item.filePath}"
        viewModel.getImagePath(fullImagePath)
    }


}