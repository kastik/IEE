package com.kastik.apps.core.di

import com.google.firebase.messaging.FirebaseMessaging
import com.kastik.apps.core.domain.service.Notifier
import com.kastik.apps.core.notifications.NotifierImpl
import com.kastik.apps.core.notifications.PushNotificationsDatasource
import com.kastik.apps.core.notifications.PushNotificationsDatasourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationsModule {

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseInstance(): FirebaseMessaging =
            FirebaseMessaging.getInstance()
    }

    @Binds
    @Singleton
    internal abstract fun bindNotificationDataSource(
        pushNotificationsDatasourceImpl: PushNotificationsDatasourceImpl
    ): PushNotificationsDatasource


    @Binds
    @Singleton
    internal abstract fun bindNotifier(
        notifierImpl: NotifierImpl
    ): Notifier

}