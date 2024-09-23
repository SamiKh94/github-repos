package com.githubrepos.app.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.githubrepos.app.R
import com.githubrepos.app.databinding.ActivityMainBinding
import com.githubrepos.app.ui.repository.FavoriteRepositoriesActivity
import com.githubrepos.app.ui.repository.RepositoriesViewModel
import com.githubrepos.app.ui.repository.details.RepositoryDetailsActivity.Companion.REPO_ITEM_ARG
import com.githubrepos.app.ui.repository.details.RepositoryDetailsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val viewModel: RepositoriesViewModel by viewModels()

    private var isTwoPane: Boolean = false
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initViews()

        observeViewModelStates()
    }

    private fun observeViewModelStates() {
        with(viewModel) {
            lifecycleScope.launch {
                itemSelectionStateFlow.collectLatest {
                    if (isTwoPane && it != null) {
                        binding.detailFragmentContainer.isVisible = true
                        // For tablets, also load the detail fragment with default content
                        supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.detail_fragment_container,
                                RepositoryDetailsFragment().apply {
                                    arguments = Bundle().apply {
                                        putParcelable(REPO_ITEM_ARG, it)
                                    }
                                })
                            .commit()
                    }
                }
            }
        }
    }

    private fun initViews() {
        binding.lifecycleOwner = this

        setActionBar(binding.toolbar)
        actionBar?.title = getString(R.string.all_repositories)

        isTwoPane = resources.getBoolean(R.bool.isTwoPane)

        binding.floatingActionButton.setOnClickListener {
            startActivity(FavoriteRepositoriesActivity.newIntent(this))
        }
    }
}