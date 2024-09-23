package com.githubrepos.app.ui.repository.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import com.githubrepos.app.R
import com.githubrepos.app.data.remote.RepositoryItem
import com.githubrepos.app.databinding.ActivityRepositoryDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoryDetailsActivity : FragmentActivity() {

    private lateinit var binding: ActivityRepositoryDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_repository_details)
        setActionBar(binding.toolbar)
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

        initViews()
    }

    private fun initViews() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frameLayout, RepositoryDetailsFragment().apply {
            val bundle = Bundle()
            bundle.putParcelable(REPO_ITEM_ARG, getRepositoryItem())
            arguments = bundle
        }).commit()
    }

    private fun getRepositoryItem(): RepositoryItem = intent.getParcelableExtra(REPO_ITEM_EXTRA)!!

    companion object {
        const val REPO_ITEM_ARG = "_REPO_ITEM_ARG"
        const val REPO_ITEM_EXTRA = "_REPO_ITEM_ARG"
        fun newIntent(context: Context, repositoryItem: RepositoryItem) =
            Intent(context, RepositoryDetailsActivity::class.java).apply {
                putExtra(REPO_ITEM_EXTRA, repositoryItem)
            }
    }
}