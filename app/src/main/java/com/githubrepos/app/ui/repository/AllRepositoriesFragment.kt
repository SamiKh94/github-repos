package com.githubrepos.app.ui.repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.githubrepos.app.R
import com.githubrepos.app.databinding.FragmentRepositoriesListBinding
import com.githubrepos.app.domain.models.CreationPeriod
import com.githubrepos.app.ui.repository.details.RepositoryDetailsActivity
import com.githubrepos.app.utils.SpacesItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
@AndroidEntryPoint
class AllRepositoriesFragment : Fragment() {

    private var isTwoPane: Boolean = false

    private lateinit var binding: FragmentRepositoriesListBinding
    private val sharedViewModel by activityViewModels<RepositoriesViewModel>()

    private val repositoriesAdapter: PagedRepositoriesAdapter =
        PagedRepositoriesAdapter(onAddToFavReposClicked = {
            sharedViewModel.markRepositoryAsFavorite(it)
            updatePaginationData()
        }, onRepositoryClicked = {
            if (isTwoPane) {
                sharedViewModel.itemSelectionStateFlow.value = it
                return@PagedRepositoriesAdapter
            }
            startActivity(RepositoryDetailsActivity.newIntent(requireContext(), it))
        })

    private fun updatePaginationData() {
        repositoriesAdapter.refresh()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepositoriesListBinding.inflate(inflater)

        isTwoPane = resources.getBoolean(R.bool.isTwoPane)

        with(binding.list) {

            layoutManager = LinearLayoutManager(context)
            adapter = repositoriesAdapter
                .withLoadStateHeaderAndFooter(
                    header = RepositoriesLoadStateAdapter(),
                    footer = RepositoriesLoadStateAdapter()
                )


            repositoriesAdapter.addLoadStateListener {
                when (it.refresh) {
                    is LoadState.NotLoading -> {
                        binding.isLoading = false
                    }

                    is LoadState.Loading -> {
                        binding.isLoading = true
                        it.append.endOfPaginationReached && repositoriesAdapter.itemCount > 1
                    }

                    is LoadState.Error -> {
                        if (repositoriesAdapter.itemCount < 1) {
                            binding.isLoading = false
                        }
                    }
                }
            }

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
            lifecycleOwner = viewLifecycleOwner
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

                        sharedViewModel.updateCreationPeriodSelection(value = creationPeriodSelection)

                    } else {
                        sharedViewModel.updateCreationPeriodSelection(value = CreationPeriod.A_MONTH)
                    }
                }
            }
        }
    }

    private fun observeViewModelStates() {
        viewLifecycleOwner.lifecycleScope.launch {

            sharedViewModel.repositoriesResultUIState.collectLatest {
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