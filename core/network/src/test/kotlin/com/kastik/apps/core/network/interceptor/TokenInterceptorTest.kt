package com.kastik.apps.core.network.interceptor


import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.Request
import org.junit.Before
import org.junit.Test


class TokenInterceptorTest {
    lateinit var tokenProvider: FakeTokenManager
    lateinit var interceptor: TokenInterceptor
    val request = Request.Builder().url("http://site.com").build()
    val chain = FakeInterceptorChain(request)

    @Before
    fun setUp() {
        tokenProvider = FakeTokenManager()
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
    fun addHeaderWhenTokenIsAvailableTest() = runTest {
        tokenProvider.updateToken("token")
        interceptor.intercept(chain)
        assertThat(chain.proceededRequest).isNotNull()
        assertThat(chain.proceededRequest!!.header("Authorization")).isNotNull()
        assertThat(chain.proceededRequest!!.header("Authorization")).isEqualTo("Bearer token")
    }


    @Test
    fun reactsToTokenUpdatesTest() = runTest {
        interceptor.intercept(chain)
        assertThat(chain.proceededRequest).isNotNull()
        assertThat(chain.proceededRequest!!.header("Authorization")).isNull()

        tokenProvider.updateToken("token")

        interceptor.intercept(chain)

        assertThat(chain.proceededRequest).isNotNull()
        assertThat(chain.proceededRequest!!.header("Authorization")).isNotNull()
        assertThat(chain.proceededRequest!!.header("Authorization")).isEqualTo("Bearer token")
    }
}
