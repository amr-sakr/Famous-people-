package com.example.famouspeople.features.peopleList.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.famouspeople.R
import com.example.famouspeople.databinding.PagingStateLayoutBinding
import com.example.famouspeople.databinding.PeopleListItemBinding
import com.example.famouspeople.features.peopleList.ui.modelClass.ViewPerson
import com.example.famouspeople.util.IMAGES_URL
import com.example.famouspeople.util.NetworkState
import com.example.famouspeople.util.Status
import timber.log.Timber

class PeoplePagedListAdapter(
    private val listener: OnPeopleClickListener,
    private val retryCallback: () -> Unit
) :
    PagedListAdapter<ViewPerson, RecyclerView.ViewHolder>(PeopleDiffUtil()) {

    private var networkState: NetworkState? = null

    companion object {
        const val PEOPLE_HOLDER_VIEW_TYPE = 0
        const val NETWORK_STATE_HOLDER_VIEW_TYPE = 1
    }

    interface OnPeopleClickListener {
        fun onItemClick(item: ViewPerson)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PEOPLE_HOLDER_VIEW_TYPE -> PeopleViewHolder.from(parent, listener)
            NETWORK_STATE_HOLDER_VIEW_TYPE -> PeopleNetworkStateViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Timber.d("in onBindViewHolder ")
        when (holder) {
            is PeopleViewHolder -> {
                Timber.d("in viewHolder 1")
                val item = getItem(position)
                item?.let {
                    Timber.d("in viewHolder 2")
                    holder.bind(item)
                }
            }

            is PeopleNetworkStateViewHolder -> {
                holder.bind(retryCallback, networkState)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_STATE_HOLDER_VIEW_TYPE
        } else {
            PEOPLE_HOLDER_VIEW_TYPE
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    class PeopleViewHolder(
        private val binding: PeopleListItemBinding,
        private val listener: OnPeopleClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ViewPerson) {
            itemView.setOnClickListener {
                listener.onItemClick(item)
            }
            item.apply {
                binding.tvName.text = item.name
                if (gender == 1) {
                    binding.tvGender.text = itemView.context.getString(R.string.gender_female)
                } else {
                    binding.tvGender.text = itemView.context.getString(R.string.gender_male)
                }

                binding.tvPopularity.text = popularity.toString()


                Glide.with(itemView.context)
                    .load(IMAGES_URL + profilePath)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.ic_baseline_person_24)
                            .error(R.drawable.ic_baseline_person_24)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                    .into(binding.ivProfilePhoto)
            }
        }


        companion object {
            fun from(parent: ViewGroup, listener: OnPeopleClickListener): PeopleViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PeopleListItemBinding.inflate(layoutInflater, parent, false)
                return PeopleViewHolder(binding, listener)
            }
        }
    }


    class PeopleNetworkStateViewHolder(
        var binding: PagingStateLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(retryCallback: () -> Unit, networkState: NetworkState?) {
            itemView.apply {
                binding.loading.visibility =
                    if (networkState?.status == Status.RUNNING) View.VISIBLE else View.GONE
                binding.btnRetry.visibility =
                    if (networkState?.status == Status.FAILED) View.VISIBLE else View.GONE
                binding.message.visibility =
                    if (networkState?.errorMessageRes != null) View.VISIBLE else View.GONE
                if (networkState?.errorMessageRes != null) {
                    binding.message.text = networkState.errorMessage
                }
                binding.btnRetry.setOnClickListener {
                    retryCallback()
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): PeopleNetworkStateViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PagingStateLayoutBinding.inflate(layoutInflater, parent, false)
                return PeopleNetworkStateViewHolder(binding)
            }

        }

    }
}

class PeopleDiffUtil : DiffUtil.ItemCallback<ViewPerson>() {
    override fun areItemsTheSame(oldItem: ViewPerson, newItem: ViewPerson): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ViewPerson, newItem: ViewPerson): Boolean {
        return oldItem == newItem
    }

}