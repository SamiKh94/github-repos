package com.githubrepos.app.ui.repository

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.githubrepos.app.R
import com.githubrepos.app.data.remote.RepositoryItem
import com.githubrepos.app.databinding.ListItemRepositoryBinding

class RepositoryViewHolder(private val binding: ListItemRepositoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            Log.d("Click", "Click")
        }
    }

    fun bind(repository: RepositoryItem) {
        with(binding) {
            ownerAvatarUrl = repository.owner.avatarUrl
            repoName.text = repository.name
            repoDesc.text =
                if (repository.description.isNullOrBlank()) binding.root.context.getString(
                    R.string.no_description_placeholder
                ) else repository.description

            star.text = repository.stars.toString()
            ownerName.text = binding.root.context.getString(
                R.string.owner_name, repository.owner.login
            )
        }
    }

    companion object {
        fun create(parent: ViewGroup): RepositoryViewHolder {
            return RepositoryViewHolder(ListItemRepositoryBinding.inflate(LayoutInflater.from(parent.context)))
        }
    }
}