package com.githubrepos.app.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.githubrepos.app.R
import com.githubrepos.app.databinding.ActivityMainBinding
import com.githubrepos.app.ui.repository.FavoriteRepositoriesActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setActionBar(binding.toolbar)

        initViews()
    }

    private fun initViews() {

        binding.floatingActionButton.setOnClickListener {
            startActivity(FavoriteRepositoriesActivity.newIntent(this))
        }
    }
}