package com.kastik.apps.core.testing.interseptor

import com.kastik.apps.core.di.TokenProvider
import com.kastik.apps.core.testing.datasource.local.FakeAuthenticationLocalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.Connection
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response


class FakeTokenProvider(initial: String? = null) {

    private val fakeLocal = FakeAuthenticationLocalDataSource()

    private val scope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())

    val provider = TokenProvider(
        localDataSource = fakeLocal,
        appScope = scope
    )

    init {
        runBlocking {
            initial?.let {
                fakeLocal.saveAboardToken(initial)
            }
        }

    }

    fun emit(value: String?) {
        fakeLocal.emitToken(value)
    }
}

class FakeInterceptorChain(
    private val request: Request
) : Interceptor.Chain {

    var proceededRequest: Request? = null

    override fun request(): Request = request

    override fun proceed(request: Request): Response {
        proceededRequest = request
        return Response.Builder()
            .code(200)
            .message("OK")
            .protocol(Protocol.HTTP_1_1)
            .request(request)
            .build()
    }

    override fun connection(): Connection? = null
    override fun call(): Call = throw NotImplementedError()
    override fun connectTimeoutMillis(): Int = 0
    override fun withConnectTimeout(timeout: Int, unit: java.util.concurrent.TimeUnit) = this
    override fun readTimeoutMillis(): Int = 0
    override fun withReadTimeout(timeout: Int, unit: java.util.concurrent.TimeUnit) = this
    override fun writeTimeoutMillis(): Int = 0
    override fun withWriteTimeout(timeout: Int, unit: java.util.concurrent.TimeUnit) = this
}
