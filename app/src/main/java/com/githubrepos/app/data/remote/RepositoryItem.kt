package com.githubrepos.app.data.remote

import android.os.Parcelable
import com.githubrepos.app.di.RepositoryEntity
import com.githubrepos.app.domain.models.Owner
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

@Parcelize
data class RepositoryItem(
    val id: Int,
    val name: String,
    val description: String?,
    val ownerLoginName: String,
    val ownerAvatarUrl: String?,
    val stars: Int,
    val forks: Int,
    val githubLink: String,
    val language: String?,
    val createdAt: String,
    val isFavorite: Boolean = false
) : Parcelable {


    val formattedCreatedAtDate: String
        get() {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val date: Date? = inputFormat.parse(createdAt)
            val outputFormat = SimpleDateFormat("MMM dd, yyyy HH:mm")
            val formattedDate = outputFormat.format(date)
            return formattedDate
        }
}


fun RepositoryItem.asEntity(): RepositoryEntity {
    return RepositoryEntity(
        id = id,
        name = name,
        description = description,
        ownerLoginName = ownerLoginName,
        ownerAvatarUrl = ownerAvatarUrl,
        stars = stars,
        forks = forks,
        githubLink = githubLink,
        language = language,
        createdAt = createdAt
    )
}