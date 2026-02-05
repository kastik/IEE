package com.kastik.apps.core.notifications

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface PushNotificationsDatasource {
    suspend fun subscribeToTopics(ids: List<Int>)
    suspend fun unsubscribeFromTopics(ids: List<Int>)
    suspend fun unsubscribeFromAllTopics()
}


internal class PushNotificationsDatasourceImpl @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging
) : PushNotificationsDatasource {

    override suspend fun subscribeToTopics(ids: List<Int>) {
        coroutineScope {
            ids.forEach { id ->
                launch {
                    firebaseMessaging.subscribeToTopic(id.toString())
                }
            }
        }
    }

    override suspend fun unsubscribeFromTopics(ids: List<Int>) {
        coroutineScope {
            ids.forEach { id ->
                launch {
                    firebaseMessaging.unsubscribeFromTopic(id.toString())
                }
            }
        }
    }

    override suspend fun unsubscribeFromAllTopics() {
        firebaseMessaging.deleteToken().await()
        FirebaseMessaging.getInstance().token.await()
    }

}