package com.kastik.apps.core.network.model.request

import kotlinx.serialization.Serializable

@Serializable data class SubscribeDto(val tags: List<Int>)
