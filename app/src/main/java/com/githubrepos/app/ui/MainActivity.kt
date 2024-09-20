package com.githubrepos.app.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.githubrepos.app.R
import com.githubrepos.app.databinding.ActivityMainBinding
import com.githubrepos.app.ui.repository.RepositoriesViewModel
import com.githubrepos.app.ui.repository.RepositoryUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<RepositoriesViewModel>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        observeViewModelStates()
    }

    private fun observeViewModelStates() {

        lifecycleScope.launchWhenStarted {
            viewModel.repositoriesUiState.collect {
                when (it) {
                    is RepositoryUiState.Error -> {}
                    is RepositoryUiState.Loading -> {}
                    is RepositoryUiState.Success -> {
                        Log.d("Size", "Size: ${it.repositories.size}")
                    }
                }
            }
        }
    }
}