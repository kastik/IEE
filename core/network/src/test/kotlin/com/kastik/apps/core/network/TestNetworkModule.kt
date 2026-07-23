package com.kastik.apps.core.network

import com.kastik.apps.core.network.datasource.AnnouncementRemoteDataSourceImplTest
import com.kastik.apps.core.network.datasource.AuthenticationRemoteDataSourceImplTest
import com.kastik.apps.core.network.datasource.AuthorRemoteDataSourceImplTest
import com.kastik.apps.core.network.datasource.ProfileRemoteDataSourceImplTest
import com.kastik.apps.core.network.datasource.TagsRemoteDataSourceImplTest
import com.kastik.apps.core.network.interceptor.TokenInterceptorTest
import com.kastik.apps.core.network.serializers.IntToBooleanSerializerTest
import com.kastik.apps.core.network.serializers.IntToSortTypeSerializerTest
import com.kastik.apps.core.network.serializers.StringToInstantSerializerTest
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
    IntToSortTypeSerializerTest::class,
    IntToBooleanSerializerTest::class,
    StringToInstantSerializerTest::class,
)
class TestNetworkModule
