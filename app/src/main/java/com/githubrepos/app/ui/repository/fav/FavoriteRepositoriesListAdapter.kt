package com.githubrepos.app.ui.repository.fav

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.githubrepos.app.data.remote.RepositoryItem
import com.githubrepos.app.ui.repository.RepositoryViewHolder

class FavoriteRepositoriesListAdapter(
    private val onRepositoryClicked: (RepositoryItem) -> Unit,
    private val onAddToFavReposClicked: (RepositoryItem) -> Unit
) : ListAdapter<RepositoryItem, RepositoryViewHolder>(REPO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        return RepositoryViewHolder.create(parent, onAddToFavReposClicked, onRepositoryClicked)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val currentRepository = getItem(position)
        holder.bind(currentRepository)
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<RepositoryItem>() {
            override fun areItemsTheSame(
                oldItem: RepositoryItem,
                newItem: RepositoryItem
            ): Boolean {
                return oldItem.id == newItem.id // Or some unique identifier
            }

            override fun areContentsTheSame(
                oldItem: RepositoryItem,
                newItem: RepositoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}