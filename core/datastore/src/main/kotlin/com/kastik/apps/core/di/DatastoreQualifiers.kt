package com.kastik.apps.core.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthDatastore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserPrefsDatastore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserProfileDatastore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserSubscriptionsDatastore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserSubscribableTagsDatastore