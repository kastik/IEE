package com.kastik.apps.core.network.interceptor

import kotlinx.coroutines.flow.MutableStateFlow

class FakeTokenProvider : TokenProvider {
    override val token: MutableStateFlow<String?> = MutableStateFlow(null)
}
