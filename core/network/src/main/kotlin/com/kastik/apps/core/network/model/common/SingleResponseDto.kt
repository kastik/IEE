package com.kastik.apps.core.network.model.common

import kotlinx.serialization.Serializable

@Serializable data class SingleResponseDto<T>(val data: T)
