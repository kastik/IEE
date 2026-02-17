package com.kastik.apps.core.data.provider

import com.kastik.apps.core.network.interceptor.TokenProvider
import kotlinx.coroutines.flow.MutableStateFlow


class FakeTokenProvider : TokenProvider {
    override val token: MutableStateFlow<String?> = MutableStateFlow(null)
}

