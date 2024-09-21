package com.githubrepos.app.utils

import com.githubrepos.app.domain.models.CreationPeriod
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class QueryBuilder(creationPeriod: CreationPeriod) {
    private val date: LocalDate =
        LocalDate.now()
            .minusDays(if (creationPeriod == CreationPeriod.A_DAY) 1 else 0)
            .minusMonths(if (creationPeriod == CreationPeriod.A_MONTH) 1 else 0)
            .minusWeeks(if (creationPeriod == CreationPeriod.A_WEEK) 1 else 0)
    private var sort: String = "stars"
    private var order: String = "desc"

    fun sort(sort: String): QueryBuilder {
        this.sort = sort
        return this
    }

    fun order(order: String): QueryBuilder {
        this.order = order
        return this
    }

    fun build(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = date.format(formatter)

        return "created:>$formattedDate"
    }
}