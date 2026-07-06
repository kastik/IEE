package com.kastik.apps.core.model.sync

sealed class SyncState {
    object Idle : SyncState()
    object Syncing : SyncState()
    object Enqueued : SyncState()
    object Blocked : SyncState()
    object Success : SyncState()
    data object Error : SyncState()
}


val SyncState.isActive: Boolean
    get() = when (this) {
        SyncState.Syncing,
        SyncState.Enqueued,
        SyncState.Blocked -> true

        SyncState.Error,
        SyncState.Idle,
        SyncState.Success -> false
    }