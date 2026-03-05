package com.kastik.apps.core.di

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.kastik.apps.core.crashlytics.Crashlytics
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
            crashlytics: Crashlytics,
            @ApplicationContext context: Context
        ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("auth_prefs") },
            corruptionHandler = ReplaceFileCorruptionHandler { exception ->
                crashlytics.recordException(exception)
                emptyPreferences()
            },
            migrations = listOf(
                object : DataMigration<Preferences> {
                    private val EXPIRATION_KEY = intPreferencesKey("aboard_access_token_expiration")
                    private val REFRESH_TIME_KEY =
                        longPreferencesKey("aboard_access_token_last_refresh_time")

                    override suspend fun shouldMigrate(currentData: Preferences): Boolean {
                        return currentData.contains(EXPIRATION_KEY) || currentData.contains(
                            REFRESH_TIME_KEY
                        )
                    }

                    override suspend fun migrate(currentData: Preferences): Preferences {
                        val mutablePrefs = currentData.toMutablePreferences()
                        mutablePrefs.remove(EXPIRATION_KEY)
                        mutablePrefs.remove(REFRESH_TIME_KEY)
                        return mutablePrefs.toPreferences()
                    }

                    override suspend fun cleanUp() {
                    }
                }
            )
        )

        @Provides
        @Singleton
        @UserPrefsDatastore
        fun provideUserPreferencesDataStore(
            crashlytics: Crashlytics,
            @ApplicationContext context: Context,
        ): DataStore<UserPreferences> = DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = { context.dataStoreFile("user_prefs.pb") },
            corruptionHandler = ReplaceFileCorruptionHandler { exception ->
                crashlytics.recordException(exception)
                UserPreferences.getDefaultInstance()
            },
            migrations = listOf(
                object : DataMigration<UserPreferences> {
                    override suspend fun shouldMigrate(currentData: UserPreferences) =
                        currentData.announcementAlertIntervalMinutes == 0

                    override suspend fun migrate(currentData: UserPreferences) =
                        currentData.toBuilder()
                            .setAnnouncementAlertIntervalMinutes(60)
                            .build()

                    override suspend fun cleanUp() {}
                }
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
                ProfileProto.getDefaultInstance()
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
                SubscriptionsProto.getDefaultInstance()
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
                SubscribableTagsProto.getDefaultInstance()
            },
        )
    }
}