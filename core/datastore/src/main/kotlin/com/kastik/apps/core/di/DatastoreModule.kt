package com.kastik.apps.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.kastik.apps.core.datastore.AuthenticationLocalDataSource
import com.kastik.apps.core.datastore.AuthenticationLocalDataSourceImpl
import com.kastik.apps.core.datastore.PreferencesLocalDataSource
import com.kastik.apps.core.datastore.PreferencesLocalDataSourceImpl
import com.kastik.apps.core.datastore.ProfileLocalDataSource
import com.kastik.apps.core.datastore.ProfileLocalDataSourceImpl
import com.kastik.apps.core.datastore.TagsLocalDataSource
import com.kastik.apps.core.datastore.TagsLocalDataSourceImpl
import com.kastik.apps.core.datastore.proto.ProfileProto
import com.kastik.apps.core.datastore.proto.SubscribableTagsProto
import com.kastik.apps.core.datastore.proto.SubscriptionsProto
import com.kastik.apps.core.datastore.proto.UserPreferences
import com.kastik.apps.core.datastore.serializers.ProfileSerializer
import com.kastik.apps.core.datastore.serializers.SubscribableTagsSerializer
import com.kastik.apps.core.datastore.serializers.SubscribedTagsSerializer
import com.kastik.apps.core.datastore.serializers.UserPreferencesSerializer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {

    @Binds
    @Singleton
    internal abstract fun bindAuthenticationLocalDataSource(
        impl: AuthenticationLocalDataSourceImpl
    ): AuthenticationLocalDataSource

    @Binds
    @Singleton
    internal abstract fun bindUserPreferencesLocalDataSource(
        impl: PreferencesLocalDataSourceImpl
    ): PreferencesLocalDataSource

    @Binds
    @Singleton
    internal abstract fun bindUserProfileLocalDataSource(
        impl: ProfileLocalDataSourceImpl
    ): ProfileLocalDataSource

    @Binds
    @Singleton
    internal abstract fun bindSubscribableTagsDataSource(
        impl: TagsLocalDataSourceImpl
    ): TagsLocalDataSource

    companion object {
        @Provides
        @Singleton
        @AuthDatastore
        fun provideAuthPreferencesDataStore(
            @ApplicationContext context: Context
        ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("auth_prefs") }
        )

        @Provides
        @Singleton
        @UserPrefsDatastore
        fun provideUserPreferencesDataStore(
            @ApplicationContext context: Context
        ): DataStore<UserPreferences> = DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = { context.dataStoreFile("user_prefs.pb") }
        )

        @Provides
        @Singleton
        @UserProfileDatastore
        fun provideUserProfileDataStore(
            @ApplicationContext context: Context
        ): DataStore<ProfileProto> = DataStoreFactory.create(
            serializer = ProfileSerializer,
            produceFile = { context.dataStoreFile("user_profile.pb") }
        )

        @Provides
        @Singleton
        @UserSubscriptionsDatastore
        fun provideUserSubscriptionsDatastore(
            @ApplicationContext context: Context
        ): DataStore<SubscriptionsProto> = DataStoreFactory.create(
            serializer = SubscribedTagsSerializer,
            produceFile = { context.dataStoreFile("user_subscriptions.pb") }
        )

        @Provides
        @Singleton
        @UserSubscribableTagsDatastore
        fun provideSubscribableTagsDatastore(
            @ApplicationContext context: Context
        ): DataStore<SubscribableTagsProto> = DataStoreFactory.create(
            serializer = SubscribableTagsSerializer,
            produceFile = { context.dataStoreFile("subscribable_tags_tags.pb") }
        )
    }
}