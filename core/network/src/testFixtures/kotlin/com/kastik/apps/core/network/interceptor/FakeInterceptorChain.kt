package com.kastik.apps.core.network.interceptor

import okhttp3.Authenticator
import okhttp3.Cache
import okhttp3.Call
import okhttp3.CertificatePinner
import okhttp3.Connection
import okhttp3.ConnectionPool
import okhttp3.CookieJar
import okhttp3.Dns
import okhttp3.EventListener
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import java.net.Proxy
import java.net.ProxySelector
import java.util.concurrent.TimeUnit
import javax.net.SocketFactory
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

class FakeInterceptorChain(private val request: Request) : Interceptor.Chain {

    var proceededRequest: Request? = null
    override val followSslRedirects: Boolean
        get() = TODO("Not yet implemented")

    override val followRedirects: Boolean
        get() = TODO("Not yet implemented")

    override val dns: Dns
        get() = TODO("Not yet implemented")

    override val socketFactory: SocketFactory
        get() = TODO("Not yet implemented")

    override val retryOnConnectionFailure: Boolean
        get() = TODO("Not yet implemented")

    override val authenticator: Authenticator
        get() = TODO("Not yet implemented")

    override val cookieJar: CookieJar
        get() = TODO("Not yet implemented")

    override val cache: Cache?
        get() = TODO("Not yet implemented")

    override val proxy: Proxy?
        get() = TODO("Not yet implemented")

    override val proxySelector: ProxySelector
        get() = TODO("Not yet implemented")

    override val proxyAuthenticator: Authenticator
        get() = TODO("Not yet implemented")

    override val sslSocketFactoryOrNull: SSLSocketFactory?
        get() = TODO("Not yet implemented")

    override val x509TrustManagerOrNull: X509TrustManager?
        get() = TODO("Not yet implemented")

    override val hostnameVerifier: HostnameVerifier
        get() = TODO("Not yet implemented")

    override val certificatePinner: CertificatePinner
        get() = TODO("Not yet implemented")

    override val connectionPool: ConnectionPool
        get() = TODO("Not yet implemented")

    override val eventListener: EventListener
        get() = TODO("Not yet implemented")

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

    override fun withConnectTimeout(timeout: Int, unit: TimeUnit) = this

    override fun readTimeoutMillis(): Int = 0

    override fun withReadTimeout(timeout: Int, unit: TimeUnit) = this

    override fun writeTimeoutMillis(): Int = 0

    override fun withWriteTimeout(timeout: Int, unit: TimeUnit) = this

    override fun withDns(dns: Dns): Interceptor.Chain {
        TODO("Not yet implemented")
    }

    override fun withSocketFactory(socketFactory: SocketFactory): Interceptor.Chain {
        TODO("Not yet implemented")
    }

    override fun withRetryOnConnectionFailure(
        retryOnConnectionFailure: Boolean
    ): Interceptor.Chain {
        TODO("Not yet implemented")
    }

    override fun withAuthenticator(authenticator: Authenticator): Interceptor.Chain {
        TODO("Not yet implemented")
    }

    override fun withCookieJar(cookieJar: CookieJar): Interceptor.Chain {
        TODO("Not yet implemented")
    }

    override fun withCache(cache: Cache?): Interceptor.Chain {
        TODO("Not yet implemented")
    }

    override fun withProxy(proxy: Proxy?): Interceptor.Chain {
        TODO("Not yet implemented")
    }

    override fun withProxySelector(proxySelector: ProxySelector): Interceptor.Chain {
        TODO("Not yet implemented")
    }

    override fun withProxyAuthenticator(proxyAuthenticator: Authenticator): Interceptor.Chain {
        TODO("Not yet implemented")
    }

    override fun withSslSocketFactory(
        sslSocketFactory: SSLSocketFactory?,
        x509TrustManager: X509TrustManager?,
    ): Interceptor.Chain {
        TODO("Not yet implemented")
    }

    override fun withHostnameVerifier(hostnameVerifier: HostnameVerifier): Interceptor.Chain {
        TODO("Not yet implemented")
    }

    override fun withCertificatePinner(certificatePinner: CertificatePinner): Interceptor.Chain {
        TODO("Not yet implemented")
    }

    override fun withConnectionPool(connectionPool: ConnectionPool): Interceptor.Chain {
        TODO("Not yet implemented")
    }
}
