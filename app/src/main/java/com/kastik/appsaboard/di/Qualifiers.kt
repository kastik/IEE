package com.kastik.appsaboard.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AnnRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthOkHttp

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AnnOkHttp