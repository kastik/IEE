package com.kastik.apps.core.di

import com.google.firebase.messaging.FirebaseMessaging
import com.kastik.apps.core.notifications.PushNotificationsDatasource
import com.kastik.apps.core.notifications.PushNotificationsDatasourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationsModule {

    @Provides
    @Singleton
    fun provideFirebaseInstance(): FirebaseMessaging =
        FirebaseMessaging.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseNotificationDataSource(
        firebaseMessaging: FirebaseMessaging
    ): PushNotificationsDatasource =
        PushNotificationsDatasourceImpl(firebaseMessaging)

}