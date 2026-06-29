package com.kastik.apps.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.kastik.apps.core.crashlytics.Crashlytics
import com.kastik.apps.core.datastore.datasource.AuthenticationLocalDataSource
import com.kastik.apps.core.datastore.datasource.AuthenticationLocalDataSourceImpl
import com.kastik.apps.core.datastore.datasource.PreferencesLocalDataSource
import com.kastik.apps.core.datastore.datasource.PreferencesLocalDataSourceImpl
import com.kastik.apps.core.datastore.datasource.ProfileLocalDataSource
import com.kastik.apps.core.datastore.datasource.ProfileLocalDataSourceImpl
import com.kastik.apps.core.datastore.datasource.TagsLocalDataSource
import com.kastik.apps.core.datastore.datasource.TagsLocalDataSourceImpl
import com.kastik.apps.core.datastore.migrations.MigrateInterval
import com.kastik.apps.core.datastore.migrations.RemoveExpirationKey
import com.kastik.apps.core.datastore.migrations.RemoveRefreshKey
import com.kastik.apps.core.datastore.proto.ProfileProto
import com.kastik.apps.core.datastore.proto.SubscribableTagsProto
import com.kastik.apps.core.datastore.proto.SubscriptionsProto
import com.kastik.apps.core.datastore.proto.UserPreferencesProto
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
            crashlytics: Crashlytics,
            @ApplicationContext context: Context
        ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("auth_prefs") },
            corruptionHandler = ReplaceFileCorruptionHandler { exception ->
                crashlytics.recordException(exception)
                emptyPreferences()
            },
            migrations = listOf(
                RemoveExpirationKey,
                RemoveRefreshKey,
            )
        )

        @Provides
        @Singleton
        @UserPrefsDatastore
        fun provideUserPreferencesDataStore(
            crashlytics: Crashlytics,
            @ApplicationContext context: Context,
        ): DataStore<UserPreferencesProto> = DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = { context.dataStoreFile("user_prefs.pb") },
            corruptionHandler = ReplaceFileCorruptionHandler { exception ->
                crashlytics.recordException(exception)
                UserPreferencesSerializer.defaultValue
            },
            migrations = listOf(
                MigrateInterval,
            )
        )

        @Provides
        @Singleton
        @UserProfileDatastore
        fun provideUserProfileDataStore(
            crashlytics: Crashlytics,
            @ApplicationContext context: Context
        ): DataStore<ProfileProto> = DataStoreFactory.create(
            serializer = ProfileSerializer,
            produceFile = { context.dataStoreFile("user_profile.pb") },
            corruptionHandler = ReplaceFileCorruptionHandler { exception ->
                crashlytics.recordException(exception)
                ProfileSerializer.defaultValue
            },
        )

        @Provides
        @Singleton
        @UserSubscriptionsDatastore
        fun provideUserSubscriptionsDatastore(
            crashlytics: Crashlytics,
            @ApplicationContext context: Context
        ): DataStore<SubscriptionsProto> = DataStoreFactory.create(
            serializer = SubscribedTagsSerializer,
            produceFile = { context.dataStoreFile("user_subscriptions.pb") },
            corruptionHandler = ReplaceFileCorruptionHandler { exception ->
                crashlytics.recordException(exception)
                SubscribedTagsSerializer.defaultValue
            },
        )

        @Provides
        @Singleton
        @UserSubscribableTagsDatastore
        fun provideSubscribableTagsDatastore(
            crashlytics: Crashlytics,
            @ApplicationContext context: Context
        ): DataStore<SubscribableTagsProto> = DataStoreFactory.create(
            serializer = SubscribableTagsSerializer,
            produceFile = { context.dataStoreFile("subscribable_tags_tags.pb") },
            corruptionHandler = ReplaceFileCorruptionHandler { exception ->
                crashlytics.recordException(exception)
                SubscribableTagsSerializer.defaultValue
            },
        )
    }
}