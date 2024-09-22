package com.githubrepos.app.ui.repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.githubrepos.app.R
import com.githubrepos.app.databinding.FragmentRepositoriesListBinding
import com.githubrepos.app.domain.models.CreationPeriod
import com.githubrepos.app.ui.repository.details.RepositoryDetailsActivity
import com.githubrepos.app.utils.SpacesItemDecoration
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment representing a list of Items.
 */
@AndroidEntryPoint
class AllRepositoriesFragment : Fragment() {

    private lateinit var binding: FragmentRepositoriesListBinding
    private val viewModel by viewModels<RepositoriesViewModel>()

    private val repositoriesAdapter: PagedRepositoriesAdapter =
        PagedRepositoriesAdapter(onAddToFavReposClicked = {
            viewModel.markRepositoryAsFavorite(it)
        }, onRepositoryClicked = {
            startActivity(RepositoryDetailsActivity.newIntent(requireContext(), it))
        })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        initViews()
        observeViewModelStates()
    }

    private fun initViews() {
        with(binding) {
            with(chipGroup) {
                check(R.id.chipMonth)
                setOnCheckedStateChangeListener { _, checkedIds ->
                    if (checkedIds.isNotEmpty()) {
                        val selectedChipId = checkedIds[0]

                        val creationPeriodSelection = when (selectedChipId) {
                            R.id.chipMonth -> CreationPeriod.A_MONTH

                            R.id.chipWeek -> CreationPeriod.A_WEEK

                            else -> CreationPeriod.A_DAY
                        }

                        viewModel.updateCreationPeriodSelection(value = creationPeriodSelection)

                    } else {
                        viewModel.updateCreationPeriodSelection(value = CreationPeriod.A_MONTH)
                    }
                }
            }
        }
    }

    private fun observeViewModelStates() {

        binding.lifecycleOwner = viewLifecycleOwner

        lifecycleScope.launchWhenStarted {
            viewModel.repositoriesUiState.collect {
                when (it) {
                    is RepositoryUiState.Error -> {
                        binding.isLoading = false
                    }

                    is RepositoryUiState.Loading -> {
                        binding.isLoading = true
                    }

                    is RepositoryUiState.Success -> {
                        repositoriesAdapter.submitData(it.repositories)
                        binding.isLoading = false
                    }
                }
            }
        }
    }
}