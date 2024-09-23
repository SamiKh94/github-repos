package com.githubrepos.app.ui.repository

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import com.githubrepos.app.R
import com.githubrepos.app.databinding.ActivityFavoriteRepositoriesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRepositoriesActivity : FragmentActivity() {

    private lateinit var binding: ActivityFavoriteRepositoriesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite_repositories)

        setActionBar(binding.toolbar)
        actionBar?.title = getString(R.string.favorite_repositories)

        initViews()
    }

    private fun initViews() {
        actionBar?.apply {

            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle menu item clicks
                return when (menuItem.itemId) {
                    android.R.id.home -> {
                        finish()
                        true
                    }
                    else -> false
                }
            }
        }, this, Lifecycle.State.RESUMED)

    }


    companion object {
        fun newIntent(context: Context) = Intent(context, FavoriteRepositoriesActivity::class.java)
    }
}