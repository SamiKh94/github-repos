package com.githubrepos.app.ui.repository

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.githubrepos.app.R
import com.githubrepos.app.databinding.FragmentRepositoriesListBinding
import com.githubrepos.app.utils.SpacesItemDecoration
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment representing a list of Items.
 */
@AndroidEntryPoint
class RepositoryFragment : Fragment() {

    private lateinit var binding: FragmentRepositoriesListBinding
    private val viewModel by viewModels<RepositoriesViewModel>()
    private val repositoriesAdapter: RepositoriesListAdapter = RepositoriesListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRepositoriesListBinding.inflate(inflater)

        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
            adapter = repositoriesAdapter
            addItemDecoration(
                SpacesItemDecoration()
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModelStates()
    }

    private fun observeViewModelStates() {

        binding.lifecycleOwner = viewLifecycleOwner

        lifecycleScope.launchWhenStarted {
            viewModel.repositoriesUiState.collect {
                when (it) {
                    is RepositoryUiState.Error -> {}
                    is RepositoryUiState.Loading -> {
                        binding.isLoading = true
                    }

                    is RepositoryUiState.Success -> {
                        repositoriesAdapter.submitList(it.repositories)
                        Log.d("Size", "Size: ${it.repositories.size}")
                        binding.isLoading = false
                    }
                }
            }
        }
    }


    companion object {

        @JvmStatic
        fun newInstance(columnCount: Int) =
            RepositoryFragment()
    }
}