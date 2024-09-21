package com.githubrepos.app.data.remote

import android.os.Parcelable
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
    val owner: Owner,
    val stars: Int,
    val forks: Int,
    val githubLink: String,
    val language: String?,
    val createdAt: String,
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
