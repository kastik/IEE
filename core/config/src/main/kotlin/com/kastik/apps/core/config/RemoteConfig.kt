package com.kastik.apps.core.config

interface RemoteConfig {
    suspend fun refresh()
}