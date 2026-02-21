package com.kastik.apps.core.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseAboardClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthenticatorAboardClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseAboardRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthenticatorAboardRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseAboardOkHttp

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthenticatorAboardOkHttp