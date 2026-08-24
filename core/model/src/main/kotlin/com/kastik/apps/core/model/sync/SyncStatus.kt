package com.kastik.apps.core.model.sync

sealed interface SyncStatus {
    object Idle : SyncStatus

    object Syncing : SyncStatus

    object Enqueued : SyncStatus

    object Blocked : SyncStatus

    object Success : SyncStatus

    data object Error : SyncStatus
}

val SyncStatus.isActive: Boolean
    get() =
        when (this) {
            SyncStatus.Syncing,
            SyncStatus.Enqueued,
            SyncStatus.Blocked -> true

            SyncStatus.Error,
            SyncStatus.Idle,
            SyncStatus.Success -> false
        }
