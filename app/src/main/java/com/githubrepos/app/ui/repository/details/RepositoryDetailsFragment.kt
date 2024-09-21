package com.githubrepos.app.ui.repository.details

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.lifecycle.DEFAULT_ARGS_KEY
import com.githubrepos.app.data.remote.RepositoryItem
import com.githubrepos.app.databinding.FragmentRepositoryDetailsBinding
import com.githubrepos.app.ui.repository.details.RepositoryDetailsActivity.Companion.REPO_ITEM_ARG

class RepositoryDetailsFragment : Fragment() {

    private lateinit var binding: FragmentRepositoryDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepositoryDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(arguments?.getParcelable(REPO_ITEM_ARG)!!)
    }

    private fun initViews(repositoryItem: RepositoryItem) {
        with(binding) {
            item = repositoryItem
            viewRepo.setOnClickListener {
                openUrl(requireContext(), repositoryItem.githubLink)
            }
        }
    }

    private fun openUrl(context: Context, url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }
}