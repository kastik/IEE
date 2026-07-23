package com.kastik.apps.core.network.model.common

import kotlinx.serialization.Serializable

@Serializable data class ListResponseDto<T>(val data: List<T>)
