package com.githubrepos.app.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.githubrepos.app.R
import com.githubrepos.app.databinding.ActivityMainBinding
import com.githubrepos.app.ui.repository.AllRepositoriesFragment
import com.githubrepos.app.ui.repository.fav.FavoriteRepositoriesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val allRepositoriesFragment: AllRepositoriesFragment = AllRepositoriesFragment()
    private val favoriteRepositoriesFragment: FavoriteRepositoriesFragment =
        FavoriteRepositoriesFragment()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setActionBar(binding.toolbar)

        initViews()
    }

    private fun initViews() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.home -> selectedFragment = allRepositoriesFragment
                R.id.favorites -> selectedFragment = favoriteRepositoriesFragment
            }
            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit()
            }
            true
        }
    }
}