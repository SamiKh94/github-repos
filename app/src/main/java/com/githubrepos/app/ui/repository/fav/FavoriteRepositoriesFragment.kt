package com.githubrepos.app.ui.repository.fav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.githubrepos.app.databinding.FragmentFavoriteRepositoriesBinding
import com.githubrepos.app.ui.repository.details.RepositoryDetailsActivity
import com.githubrepos.app.utils.SpacesItemDecoration
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment representing a list of Items.
 */
@AndroidEntryPoint
class FavoriteRepositoriesFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteRepositoriesBinding
    private val viewModel by viewModels<FavoriteRepositoriesViewModel>()
    private val repositoriesAdapter: FavoriteRepositoriesListAdapter =
        FavoriteRepositoriesListAdapter(onAddToFavReposClicked = {
        }, onRepositoryClicked = {
            startActivity(RepositoryDetailsActivity.newIntent(requireContext(), it))
        })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteRepositoriesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModelStates()
    }

    private fun initViews() {

        binding.searchView.addTextChangedListener {
            viewModel.performSearch(it.toString())
        }

        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
            adapter = repositoriesAdapter
            addItemDecoration(
                SpacesItemDecoration()
            )
        }
    }

    private fun observeViewModelStates() {

        binding.lifecycleOwner = viewLifecycleOwner

        lifecycleScope.launchWhenStarted {

            viewModel.favoriteRepositoriesUIState.collect {
                when (it) {
                    is FavoriteRepositoryUiState.Error -> {}
                    is FavoriteRepositoryUiState.Loading -> {
                        binding.isLoading = true
                    }

                    is FavoriteRepositoryUiState.Success -> {
                        repositoriesAdapter.submitList(it.repositories)
                        binding.isLoading = false
                    }
                }
            }
        }
    }
}