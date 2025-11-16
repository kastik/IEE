package com.kastik.apps.core.network


import com.kastik.apps.core.network.datasource.AnnouncementRemoteDataSourceImplTest
import com.kastik.apps.core.network.datasource.AuthenticationRemoteDataSourceImplTest
import com.kastik.apps.core.network.interceptor.TokenInterceptorTest
import com.kastik.apps.core.network.serializers.IntAsBooleanSerializerTest
import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
    AnnouncementRemoteDataSourceImplTest::class,
    AuthenticationRemoteDataSourceImplTest::class,
    TokenInterceptorTest::class,
    IntAsBooleanSerializerTest::class,
)

class TestNetworkModule
