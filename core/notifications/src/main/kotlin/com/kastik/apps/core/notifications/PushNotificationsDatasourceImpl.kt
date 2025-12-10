package com.kastik.apps.core.notifications

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

interface PushNotificationsDatasource {
    suspend fun subscribeToPushTags(ids: List<Int>)
    suspend fun unSubscribeFromPushTags()
}


internal class PushNotificationsDatasourceImpl(
    private val firebaseMessaging: FirebaseMessaging
) : PushNotificationsDatasource {
    override suspend fun unSubscribeFromPushTags() {
        firebaseMessaging.deleteToken().await()
        FirebaseMessaging.getInstance().token.await()
    }

    override suspend fun subscribeToPushTags(ids: List<Int>) {
        FirebaseMessaging.getInstance().deleteToken().await()
        FirebaseMessaging.getInstance().token.await()
        coroutineScope {
            ids.forEach { id ->
                launch {
                    firebaseMessaging.subscribeToTopic(id.toString()).await()
                }
            }

        }
    }
}