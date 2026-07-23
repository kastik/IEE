package com.kastik.apps.core.network.model.common

import kotlinx.serialization.Serializable

@Serializable
data class PagedResponseDto<T>(
    val data: List<T>,
    val meta: PagedMetaDto,
)
