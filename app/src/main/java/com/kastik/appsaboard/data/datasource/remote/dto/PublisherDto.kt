package com.kastik.appsaboard.data.datasource.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PublisherDto(
    val id: String,
    val name: String,
    val phone: String? = null
)