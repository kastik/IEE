package com.kastik.apps.core.network.interceptor


import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.data.provider.FakeInterceptorChain
import com.kastik.apps.core.data.provider.FakeTokenProvider
import okhttp3.Request
import org.junit.Before
import org.junit.Test


class TokenInterceptorTest {
    lateinit var tokenProvider: FakeTokenProvider
    lateinit var interceptor: TokenInterceptor
    val request = Request.Builder().url("http://site.com").build()
    val chain = FakeInterceptorChain(request)

    @Before
    fun setUp() {
        tokenProvider = FakeTokenProvider()
        interceptor = TokenInterceptor(tokenProvider)
        interceptor.intercept(chain)

    }

    @Test
    fun skipHeaderWhenTokenIsNullTest() {
        interceptor.intercept(chain)
        assertThat(chain.proceededRequest).isNotNull()
        assertThat(chain.proceededRequest!!.header("Authorization")).isNull()
    }


    @Test
    fun addHeaderWhenTokenIsAvailableTest() {
        tokenProvider.token.value = "token"
        interceptor.intercept(chain)
        assertThat(chain.proceededRequest).isNotNull()
        assertThat(chain.proceededRequest!!.header("Authorization")).isNotNull()
        assertThat(chain.proceededRequest!!.header("Authorization")).isEqualTo("Bearer token")
    }


    @Test
    fun reactsToTokenUpdatesTest() {
        interceptor.intercept(chain)
        assertThat(chain.proceededRequest).isNotNull()
        assertThat(chain.proceededRequest!!.header("Authorization")).isNull()

        tokenProvider.token.value = "token"

        interceptor.intercept(chain)

        assertThat(chain.proceededRequest).isNotNull()
        assertThat(chain.proceededRequest!!.header("Authorization")).isNotNull()
        assertThat(chain.proceededRequest!!.header("Authorization")).isEqualTo("Bearer token")
    }
}
