package com.kastik.apps.core.network.interceptor


import com.kastik.apps.core.testing.interseptor.FakeInterceptorChain
import com.kastik.apps.core.testing.interseptor.FakeTokenProvider
import okhttp3.Request
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull


class TokenInterceptorTest {
    lateinit var tokenProvider: FakeTokenProvider
    lateinit var interceptor: TokenInterceptor
    val request = Request.Builder().url("http://site.com").build()
    val chain = FakeInterceptorChain(request)

    @Before
    fun setUp() {
        tokenProvider = FakeTokenProvider(initial = null)
        interceptor = TokenInterceptor(tokenProvider.provider)
        interceptor.intercept(chain)

    }

    @Test
    fun skipHeaderWhenTokenIsNullTest() {
        interceptor.intercept(chain)
        assertNull(chain.proceededRequest!!.header("Authorization"))
    }


    @Test
    fun addHeaderWhenTokenIsAvailableTest() {
        tokenProvider.emit("token")
        interceptor.intercept(chain)
        assertEquals(
            "Bearer token",
            chain.proceededRequest!!.header("Authorization")
        )
    }


    @Test
    fun reactsToTokenUpdatesTest() {
        interceptor.intercept(chain)
        assertNull(chain.proceededRequest!!.header("Authorization"))

        tokenProvider.emit("token")

        interceptor.intercept(chain)
        assertEquals("Bearer token", chain.proceededRequest!!.header("Authorization"))
    }
}
