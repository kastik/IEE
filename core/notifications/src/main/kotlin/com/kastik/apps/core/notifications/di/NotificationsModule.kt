package com.kastik.apps.core.notifications.di

import com.google.firebase.messaging.FirebaseMessaging
import com.kastik.apps.core.notifications.Notifier
import com.kastik.apps.core.notifications.NotifierImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NotificationsModule {

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseInstance(): FirebaseMessaging = FirebaseMessaging.getInstance()
    }

    @Binds @Singleton abstract fun bindNotifier(notifierImpl: NotifierImpl): Notifier
}
