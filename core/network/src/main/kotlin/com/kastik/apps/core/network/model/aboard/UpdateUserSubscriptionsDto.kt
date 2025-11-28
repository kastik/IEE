package com.kastik.apps.core.network.model.aboard

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserSubscriptionsDto(
    val tags: List<Int>,
)