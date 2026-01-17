package com.kastik.apps.core.network


import com.kastik.apps.core.network.datasource.AnnouncementRemoteDataSourceImplTest
import com.kastik.apps.core.network.datasource.AuthenticationRemoteDataSourceImplTest
import com.kastik.apps.core.network.datasource.AuthorRemoteDataSourceImpl
import com.kastik.apps.core.network.datasource.AuthorRemoteDataSourceImplTest
import com.kastik.apps.core.network.datasource.ProfileRemoteDataSourceImplTest
import com.kastik.apps.core.network.datasource.TagsRemoteDataSourceImplTest
import com.kastik.apps.core.network.interceptor.TokenInterceptorTest
import com.kastik.apps.core.network.serializers.IntAsBooleanSerializerTest
import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
    AnnouncementRemoteDataSourceImplTest::class,
    AuthenticationRemoteDataSourceImplTest::class,
    AuthorRemoteDataSourceImplTest::class,
    ProfileRemoteDataSourceImplTest::class,
    TagsRemoteDataSourceImplTest::class,
    TokenInterceptorTest::class,
    IntAsBooleanSerializerTest::class,
)

class TestNetworkModule
