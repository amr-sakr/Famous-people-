package com.example.famouspeople.features.photoViewer

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.famouspeople.R
import com.example.famouspeople.databinding.PhotoViewerFragmentBinding

class PhotoViewerFragment : Fragment() {

    private var _binding: PhotoViewerFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PhotoViewerFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = PhotoViewerFragmentArgs.fromBundle(requireArguments())
        val imagePath = args.imagePath

        requireActivity().registerReceiver(
            onDownloadComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )

        Glide.with(requireContext())
            .load(imagePath)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .error(R.drawable.ic_baseline_person_24)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(binding.ivProfile)

        binding.materialButton.setOnClickListener {
            downloadProfile(imagePath)
        }
    }

    private var downloadId: Long = -1

    private fun downloadProfile(url: String) {
        val downloadRequest = DownloadManager.Request(Uri.parse(url))

        downloadRequest.setTitle("Profile")
            .setDescription("Downloading")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        val downloadManager =
            requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        downloadRequest.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "profile_image"
        )
        downloadRequest.setMimeType("image/*")
        downloadId = downloadManager.enqueue(downloadRequest)
    }

    private val onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id: Long = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -10)
            if (downloadId == id) {
                Toast.makeText(
                    requireContext(),
                    "Download Complete.",
                    Toast.LENGTH_SHORT
                ).show();
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        requireActivity().unregisterReceiver(onDownloadComplete)
    }

}