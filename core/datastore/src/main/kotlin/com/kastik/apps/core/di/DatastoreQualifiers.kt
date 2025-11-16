package com.kastik.apps.core.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthPreferences

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserPreferences