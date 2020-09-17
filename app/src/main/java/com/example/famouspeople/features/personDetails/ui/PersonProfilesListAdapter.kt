package com.example.famouspeople.features.personDetails.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.famouspeople.R
import com.example.famouspeople.databinding.PersonProfilesListItemBinding
import com.example.famouspeople.features.personDetails.ui.modelClass.ViewProfile
import com.example.famouspeople.util.IMAGES_URL

class PersonProfilesListAdapter(private val listener: OnProfileClickListener) :
    ListAdapter<ViewProfile, PersonProfilesListAdapter.PersonProfileViewHolder>(
        PersonProfileDiffUtil()
    ) {


    interface OnProfileClickListener {
        fun onClick(item: ViewProfile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonProfileViewHolder {
        return PersonProfileViewHolder.from(parent, listener)
    }

    override fun onBindViewHolder(holder: PersonProfileViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class PersonProfileViewHolder(
        private val binding: PersonProfilesListItemBinding,
        private val listener: OnProfileClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ViewProfile) {

            itemView.setOnClickListener {
                listener.onClick(item)
            }

            Glide.with(itemView.context)
                .load(IMAGES_URL + item.filePath)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .error(R.drawable.ic_baseline_person_24)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(binding.ivProfile)
        }


        companion object {
            fun from(parent: ViewGroup, listener: OnProfileClickListener): PersonProfileViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PersonProfilesListItemBinding.inflate(layoutInflater, parent, false)
                return PersonProfileViewHolder(binding, listener)
            }
        }

    }
}


class PersonProfileDiffUtil : DiffUtil.ItemCallback<ViewProfile>() {
    override fun areItemsTheSame(oldItem: ViewProfile, newItem: ViewProfile): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: ViewProfile, newItem: ViewProfile): Boolean {
        return oldItem.filePath == oldItem.filePath
    }

}